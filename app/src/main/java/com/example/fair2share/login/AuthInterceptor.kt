package com.example.fair2share.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fair2share.MainActivity
import com.example.fair2share.LoginActivity
import com.example.fair2share.network.AccountApi.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException


class AuthInterceptor: Interceptor {
    companion object {
        private val _shouldRestart = MutableLiveData<Boolean>()
        val shouldRestart: LiveData<Boolean>
            get() = _shouldRestart

        private val _isOffline = MutableLiveData<Boolean>()
        val isOffline: LiveData<Boolean>
            get() = _isOffline

        fun throwableIs401(throwable: Throwable) : Boolean {
            if (throwable is HttpException && throwable.code() == 401){
                return true
            }
            return false
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = sharedPreferences?.getString("token", "") ?: ""
        if (token.isNotEmpty()){
            request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", token))
                .build()
        }

        val response = chain.proceed(request)
        if (response.code() == 401) {
            setValueSafeOnBackgroundThread()
        }
        return response
    }

    private fun setValueSafeOnBackgroundThread(){
        _shouldRestart.postValue(true)
    }
}