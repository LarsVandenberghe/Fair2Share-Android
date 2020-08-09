package com.example.fair2share.startup

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository

class StartUpViewModel(val database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository: IProfileRepository = ProfileRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val shouldRelog: LiveData<Boolean> = profileRepository.shouldRelog
    val isOffline: LiveData<Boolean> = AccountApi.isOffline

    private val _token = MutableLiveData<String>(sharedPreferences.getString("token", null))
    val token: LiveData<String>
        get() = _token


    fun getProfileOnline(resouces: Resources) {
        profileRepository.updateOnStartUpCheckOnline(resouces)
    }

    fun getProfileCached() {
        profileRepository.updateWithCachedProfileOnStartUp()
    }
}