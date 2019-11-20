package com.example.fair2share.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.network.AccountApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    fun login(email: String, password : String) {
        _coroutineScope.launch {
            try {
                var getJWTDeffered = AccountApi.retrofitService.login(LoginProperty(email, password))
                AccountApi.token = getJWTDeffered.await()
                _loggedIn.value = true
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }
}
