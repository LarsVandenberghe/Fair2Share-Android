package com.example.fair2share.viewmodels.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.exceptions.CustomHttpException
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.repositories.IProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.ConnectException

class FriendListViewModel(
    private var profileArg: ProfileDTOProperty,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    val friendRequests: LiveData<List<ProfileDTOProperty>> = profileRepository.friendRequests

    init {
        if (profileArg.friends != null) {
            profileRepository.updateFromSafeArgs(profileArg)
        }
        profile.observeForever {
            profileArg = it
        }
    }

    fun update(resources: Resources) {
        _coroutineScope.launch {
            try {
                profileRepository.update()
                profileRepository.updateFriendRequestsWithApi()
            } catch (e: CustomHttpException){
                _errorMessage.postValue(resources.getString(e.stringId))
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }

    fun handleFriendRequest(userId: Long, accept: Boolean, resources: Resources) {
        _coroutineScope.launch {
            try {
                profileRepository.handleFriendRequest(userId, accept)
                _success.postValue(true)
            } catch (e: CustomHttpException){
                _errorMessage.postValue(resources.getString(e.stringId))
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }

    }
}