package com.example.fair2share.viewmodels.profile

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.repositories.IProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

class ProfileFragmentViewModel(
    private val profileRepository: IProfileRepository,
    private val activityRepository: IActivityRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _activityDeleteSuccess = MutableLiveData<Boolean>()
    val activityDeleteSuccess: LiveData<Boolean>
        get() = _activityDeleteSuccess

    fun update(resources: Resources, profile: ProfileDTOProperty? = null) {
        if (profile != null) {
            profileRepository.updateFromSafeArgs(profile)
        } else {
            _coroutineScope.launch {
                try {
                    profileRepository.update()
                } catch (e: ConnectException) {
                    AccountApi.setIsOfflineValue(true)
                    _errorMessage.postValue(resources.getString(R.string.offline_error))
                } catch (t: Throwable) {
                    _errorMessage.postValue(t.message)
                }
            }
        }
    }

    fun removeActivity(resources: Resources, activity: ActivityDTOProperty) {
        _coroutineScope.launch {
            try {
                activityRepository.removeActivity(activity.activityId!!)
                _activityDeleteSuccess.postValue(true)
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}
