package com.example.fair2share.activity.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AddEditTransactionViewModel(val activity: ActivityProperty, val transaction: TransactionProperty, val isNewTransaction: Boolean) : ViewModel() {
    private var viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun createOrUpdate(){
        _coroutineScope.launch {
            try {
                val response : Response<out Any> = if (isNewTransaction){
                    ActivityApi.retrofitService.addTransaction(activity.activityId!!, transaction).await()
                } else {
                    ActivityApi.retrofitService.updateTransaction(activity.activityId!!, transaction.transactionId!!, transaction).await()
                }

                Utils.throwExceptionIfHttpNotSuccessful(response)

                if (isNewTransaction){
                    transaction.transactionId = (response as Response<Long>).body()!!
                }

                _navigate.value = true
            } catch (e: HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e)
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }

    fun removeTransaction(){
        _coroutineScope.launch {
            val a = ActivityApi.retrofitService.removeTransaction(activity.activityId!!, transaction.transactionId!!).await()
            if (!a.isSuccessful){
                _errorMessage.value = a.errorBody()!!.charStream().toString()
            } else {
                _navigate.value = true
            }
        }
    }
}