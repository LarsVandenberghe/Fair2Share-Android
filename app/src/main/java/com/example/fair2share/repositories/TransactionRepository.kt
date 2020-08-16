package com.example.fair2share.repositories

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.database_models.TransactionDatabaseProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.utils.Constants
import com.example.fair2share.utils.Utils
import com.squareup.moshi.Moshi
import retrofit2.Response

class TransactionRepository(private val database: Fair2ShareDatabase) : ITransactionRepository {

    private val jsonAdapter = Moshi.Builder().build().adapter(TransactionDTOProperty::class.java)

    private val _transaction = MutableLiveData<TransactionDTOProperty>()
    override val transaction: LiveData<TransactionDTOProperty>
        get() = _transaction

    override suspend fun update(activityId: Long, transactionId: Long) {
        updateTransactionWithRoom(activityId, transactionId)
        updateTransactionWithApi(activityId, transactionId)
    }

    override suspend fun createOrUpdate(
        isNewTransaction: Boolean,
        activity: ActivityDTOProperty,
        transaction: TransactionFormProperty
    ) {
        val response: Response<out Any> = if (isNewTransaction) {
            ActivityApi.retrofitService.addTransaction(
                activity.activityId!!,
                transaction.makeDTO()
            ).await()
        } else {
            ActivityApi.retrofitService.updateTransaction(
                activity.activityId!!,
                transaction.transactionId!!,
                transaction.makeDTO()
            ).await()
        }

        Utils.throwExceptionIfHttpNotSuccessful(response)

        if (isNewTransaction) {
            transaction.transactionId = (response as Response<Long>).body()!!
        }
    }


    override suspend fun removeTransaction(activityId: Long, transactionId: Long) {
        val deferred =
            ActivityApi.retrofitService.removeTransaction(activityId, transactionId).await()
        if (!deferred.isSuccessful) {
            val sb = StringBuilder()
            val charStream = deferred.errorBody()?.charStream()
            if (charStream != null) {
                sb.append(charStream.readText())
            }
            throw Exception(sb.toString())
        }
    }

    override suspend fun postTransactionParticipants(
        activityId: Long,
        transactionId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    ) {
        if (toBeRemoved.isNotEmpty()) {
            val result = ActivityApi.retrofitService.removeTransactionParticipants(
                activityId,
                transactionId,
                toBeRemoved
            ).await()
            Utils.throwExceptionIfHttpNotSuccessful(result)
        }
        if (toBeAdded.isNotEmpty()) {
            val result = ActivityApi.retrofitService.addTransactionParticipants(
                activityId,
                transactionId,
                toBeAdded
            ).await()
            Utils.throwExceptionIfHttpNotSuccessful(result)
        }
    }

    private fun updateTransactionWithRoom(activityId: Long, transactionId: Long) {
        val profileId = AccountApi.sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)

        if (profileId == 0L) {
            throw Exception(Constants.SHARED_PREFERENCES_PROFILEID_NOT_SET)
        }

        Handler(Looper.getMainLooper()).post {
            Transformations.map(
                database.transactionDao.getTransaction(
                    profileId,
                    activityId,
                    transactionId
                )
            ) { property: TransactionDatabaseProperty? ->
                if (property != null) {
                    jsonAdapter.fromJson(property.data)
                } else {
                    null
                }
            }.observeForever { data: TransactionDTOProperty? ->
                if (data != null) _transaction.postValue(data)
            }
        }
    }

    private suspend fun updateTransactionWithApi(
        activityId: Long,
        transactionId: Long
    ) {
        val transaction = ActivityApi.retrofitService.getActivityTransactionById(
            activityId,
            transactionId
        ).await()
        val profileId = AccountApi.sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)

        database.transactionDao.insertTransaction(
            transaction.makeDatabaseModel(
                profileId,
                activityId
            )
        )
    }
}