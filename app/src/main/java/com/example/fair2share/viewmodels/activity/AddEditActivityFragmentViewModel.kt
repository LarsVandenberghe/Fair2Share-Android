package com.example.fair2share.viewmodels.activity

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.formdata_models.ActivityFormProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class AddEditActivityFragmentViewModel(
    val activity: ActivityFormProperty,
    private val activityRepository: IActivityRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate


    val isNewActivity: Boolean
        get() = activity.activityId == null


    fun createOrUpdate(resources: Resources) {
        _coroutineScope.launch {
            try {
                activityRepository.createOrUpdate(isNewActivity, activity)
                _navigate.postValue(true)
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (e: HttpException) {
                _errorMessage.postValue(Utils.formExceptionsToString(e))
            } catch (e: InvalidFormDataException) {
                _errorMessage.postValue(e.buildErrorMessage(resources))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}