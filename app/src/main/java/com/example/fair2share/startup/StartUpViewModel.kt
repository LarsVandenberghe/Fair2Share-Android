package com.example.fair2share.startup

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.login.AuthInterceptor
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.ProfileApi
import com.example.fair2share.network.StartUpApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class StartUpViewModel(var sharedPreferences: SharedPreferences?) : ViewModel() {
    private val _token = MutableLiveData<String>()
    val token: LiveData<String>
        get() = _token


    private val _profile = MutableLiveData<ProfileProperty>()
    val profile: LiveData<ProfileProperty>
        get() = _profile

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _shouldRelog = MutableLiveData<Boolean>()
    val shouldRelog: LiveData<Boolean>
        get() = _shouldRelog

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    init {
        _token.value = sharedPreferences?.getString("token", null)
    }


    fun getProfile() {
        _coroutineScope.launch {
            try {
                _profile.value = StartUpApi.retrofitService.profile().await()
            } catch (t: Throwable){

                if (AuthInterceptor.throwableIs401(t)){
                    _errorMessage.value = "Token expired, please relog."
                    _shouldRelog.value = true
                } else {
                    _errorMessage.value = t.message
                }
            }
        }
    }
}