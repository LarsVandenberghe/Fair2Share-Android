package com.example.fair2share.startup

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.ProfileRepository
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.network.StartUpApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class StartUpViewModel(val database: Fair2ShareDatabase) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token

    private val profileRepository = ProfileRepository(database)


    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val shouldRelog: LiveData<Boolean> = profileRepository.shouldRelog
    val isOffline: LiveData<Boolean> = AccountApi.isOffline

    init {
        _token.value = sharedPreferences.getString("token", null)
    }


    fun getProfileOnline(resouces: Resources) {
        profileRepository.updateProfileWithApi(resouces)
    }

    fun getProfileCached() {
        profileRepository.updateProfileWithRoom()
    }
}