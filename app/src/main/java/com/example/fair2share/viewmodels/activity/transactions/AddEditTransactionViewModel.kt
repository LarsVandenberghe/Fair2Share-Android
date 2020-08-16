package com.example.fair2share.viewmodels.activity.transactions

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.repositories.ITransactionRepository
import com.example.fair2share.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class AddEditTransactionViewModel(
    val activity: ActivityDTOProperty,
    val transaction: TransactionFormProperty,
    var isNewTransaction: Boolean,
    private val transactionRepository: ITransactionRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun createOrUpdate(resources: Resources) {
        _coroutineScope.launch {
            try {
                transactionRepository.createOrUpdate(isNewTransaction, activity, transaction)
                _navigate.postValue(true)
            } catch (e: HttpException) {
                _errorMessage.postValue(Utils.formExceptionsToString(e))
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (e: InvalidFormDataException) {
                _errorMessage.postValue(e.buildErrorMessage(resources))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }

    fun removeTransaction(resources: Resources) {
        _coroutineScope.launch {
            try {
                transactionRepository.removeTransaction(
                    activity.activityId!!,
                    transaction.transactionId!!
                )
                _navigate.postValue(true)
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}