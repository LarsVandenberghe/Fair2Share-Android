package com.example.fair2share.database

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.TransactionDatabaseProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.models.dto_models.asDataModel2
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.ActivityApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import java.lang.Exception
import java.lang.StringBuilder
import java.net.ConnectException

class TransactionRepository(private val database: Fair2ShareDatabase) {
    private val jsonAdapter = Moshi.Builder().build().adapter(TransactionDTOProperty::class.java)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    private val _transaction = MutableLiveData<TransactionDTOProperty>()
    val transaction: LiveData<TransactionDTOProperty>
        get() = _transaction

    private val _resetSelected = MutableLiveData<Boolean>()
    val resetSelected: LiveData<Boolean>
        get() = _resetSelected

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.IO)

    fun createOrUpdate(resources: Resources, isNewTransaction: Boolean, activity: ActivityDTOProperty, transaction: TransactionProperty){
        _coroutineScope.launch {
            try {
                val response : Response<out Any> = if (isNewTransaction){
                    ActivityApi.retrofitService.addTransaction(activity.activityId!!, transaction.makeDTO()).await()
                } else {
                    ActivityApi.retrofitService.updateTransaction(activity.activityId!!, transaction.transactionId!!, transaction.makeDTO()).await()
                }

                Utils.throwExceptionIfHttpNotSuccessful(response)

                if (isNewTransaction){
                    transaction.transactionId = (response as Response<Long>).body()!!
                }

                _navigate.postValue(true)
            } catch (e: HttpException){
                _errorMessage.postValue(Utils.formExceptionsToString(e))
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            }catch (t: Throwable){
                _errorMessage.postValue(t.message)
            }
        }
    }


    fun removeTransaction(resources: Resources, activityId: Long, transactionId:Long){
        _coroutineScope.launch {
            try {
                val deferred = ActivityApi.retrofitService.removeTransaction(activityId, transactionId).await()
                if (!deferred.isSuccessful){
                        val sb = StringBuilder()
                        val charStream = deferred.errorBody()?.charStream()
                        if (charStream != null){
                            sb.append(charStream.readText())
                        }
                        _errorMessage.postValue(sb.toString())
                } else {
                    _navigate.postValue(true)
                }
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            }
        }
    }


    fun updateTransactionWithRoom(activityId: Long, transactionId: Long){
        val profileId = AccountApi.sharedPreferences.getLong("profileId", 0L)
        if (profileId == 0L){
            throw Exception("ProfileID sharedPreferences not set!")
        }

        Transformations.map(database.transactionDao.getTransaction(profileId, activityId, transactionId)){ property : TransactionDatabaseProperty? ->
            if (property != null){
                jsonAdapter.fromJson(property.data)
            } else {
                null
            }
        }.observeForever{data: TransactionDTOProperty? ->
            if (data != null) _transaction.postValue(data)
        }
    }

    fun updateTransactionWithApi(resources: Resources, activityId: Long, transactionId: Long){
        _coroutineScope.launch {
            try {
                val transaction = ActivityApi.retrofitService.getActivityTransactionById(activityId, transactionId).await()
                val profileId = AccountApi.sharedPreferences.getLong("profileId", 0L)
                database.transactionDao.insertTransaction(transaction.makeDatabaseModel(profileId, activityId))
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable){
                _errorMessage.postValue(t.message)
            }
        }
    }


    fun postTransactionParticipants(resources: Resources, activityId:Long, transactionId: Long,toBeAdded: List<Long>, toBeRemoved: List<Long>){
        _coroutineScope.launch {
            try {
                if (toBeRemoved.size > 0) {
                    val result = ActivityApi.retrofitService.removeTransactionParticipants(activityId, transactionId, toBeRemoved).await()
                    Utils.throwExceptionIfHttpNotSuccessful(result)
                }
                if (toBeAdded.size > 0) {
                    val result = ActivityApi.retrofitService.addTransactionParticipants(activityId, transactionId, toBeAdded).await()
                    Utils.throwExceptionIfHttpNotSuccessful(result)
                }
                _success.postValue(true)
            } catch (e: HttpException){
                _errorMessage.postValue(Utils.formExceptionsToString(e))
                _resetSelected.postValue(true)
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable){
                _errorMessage.postValue(t.message)
            }
        }
    }
}