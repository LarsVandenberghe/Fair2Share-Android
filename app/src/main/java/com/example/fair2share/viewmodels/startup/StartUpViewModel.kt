package com.example.fair2share.viewmodels.startup

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

class StartUpViewModel(
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile

    private val _shouldRelog = MutableLiveData<Boolean>()
    val shouldRelog: LiveData<Boolean>
        get() = _shouldRelog

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    val isOffline: LiveData<Boolean> = AccountApi.isOffline

    private val _token = MutableLiveData<String>(
        sharedPreferences.getString(Constants.SHARED_PREFERENCES_KEY_TOKEN, null)
    )
    val token: LiveData<String>
        get() = _token


    fun getProfileOnline(resources: Resources) {
        _coroutineScope.launch {
            try {
                profileRepository.updateOnStartUpCheckOnline()
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                if (AuthInterceptor.throwableIs401(t)) {
                    _errorMessage.postValue(resources.getString(R.string.fragment_startup_tokenexipred))
                    _shouldRelog.postValue(true)
                } else {
                    _errorMessage.postValue(t.message)
                }
            }
        }
    }

    fun getProfileCached() {
        profileRepository.updateWithCachedProfileOnStartUp()
    }
}