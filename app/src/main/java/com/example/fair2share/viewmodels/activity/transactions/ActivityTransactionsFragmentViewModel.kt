package com.example.fair2share.viewmodels.activity.transactions

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.repositories.IActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

class ActivityTransactionsFragmentViewModel(
    var activityArg: ActivityDTOProperty,
    private val activityRepository: IActivityRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)
    val activity: LiveData<ActivityDTOProperty> = activityRepository.activity

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    init {
        activity.observeForever {
            activityArg = it
        }
    }

    fun update(resources: Resources) {
        _coroutineScope.launch {
            try {
                activityRepository.update(activityArg.activityId!!)
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }

    }


    fun removeActivity(resources: Resources) {
        _coroutineScope.launch {
            try {
                activityRepository.removeActivity(activityArg.activityId!!)
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