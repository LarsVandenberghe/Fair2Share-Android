package com.example.fair2share.database

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.network.ProfileApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import java.lang.Exception
import java.net.ConnectException

class ProfileRepository(private val database: Fair2ShareDatabase) {
    private val sharedPreferences: SharedPreferences = AccountApi.sharedPreferences
    private val jsonAdapter = Moshi.Builder().build().adapter(ProfileDTOProperty::class.java)
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _shouldRelog = MutableLiveData<Boolean>()
    val shouldRelog: LiveData<Boolean>
        get() = _shouldRelog


    private val _profile = MutableLiveData<ProfileDTOProperty>()
    val profile: LiveData<ProfileDTOProperty>
        get() = _profile


    fun updateFromSafeArgs(profile: ProfileDTOProperty){
        _profile.postValue(profile)
    }

    fun updateProfileWithRoom(){
        val profileId = sharedPreferences.getLong("profileId", 0L)
        if (profileId == 0L){
            return
        }

        Transformations.map(database.profileDao.getProfile(profileId)){
            jsonAdapter.fromJson(it.data)
        }.observeForever{data ->  _profile.postValue(data)}
    }

    fun updateProfileWithApi(resouces: Resources){
        _coroutineScope.launch {
            try {
                val data = ProfileApi.retrofitService.profile().await()
                _profile.postValue(data)
                sharedPreferences.edit().putLong("profileId", data.profileId)?.apply()
                database.profileDao.insertProfile(data.makeDatabaseModel())
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resouces.getString(R.string.offline_error))
            } catch (t: Throwable){
                if (AuthInterceptor.throwableIs401(t)){
                    _errorMessage.postValue(resouces.getString(R.string.fragment_startup_tokenexipred))
                    _shouldRelog.postValue(true)
                } else {
                    _errorMessage.postValue(t.message)
                }
            }
        }
    }
}