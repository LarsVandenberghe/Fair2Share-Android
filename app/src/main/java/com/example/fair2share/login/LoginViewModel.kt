package com.example.fair2share.login

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.network.AccountApi

class LoginViewModel : ViewModel() {
    suspend fun login(email: String, password : String){
        var getJWTDeffered = AccountApi.retrofitService.login(LoginProperty(email, password))
        AccountApi.token = getJWTDeffered.await()
    }
}
