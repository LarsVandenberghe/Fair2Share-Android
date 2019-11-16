package com.example.fair2share.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.network.AccountApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    //var loginData: LoginProperty? = null
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
    }

    fun login(email: String, password : String){
        coroutineScope.launch {
            try {
                var getJWTDeffered = AccountApi.retrofitService.login(LoginProperty(email, password))
                var token: String = getJWTDeffered.await()
                Log.i("LoginViewModel", token)
            } catch (t: Throwable){
                Log.e("LoginViewModel", String.format("login, onFailure called. %s", t.message))
            }
        }
    }
}
