package com.example.fair2share.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException


class AuthInterceptor : Interceptor {
    companion object {
        private val _shouldRestart = MutableLiveData<Boolean>()
        val shouldRestart: LiveData<Boolean>
            get() = _shouldRestart

        fun loginSucceeded() {
            _shouldRestart.postValue(false)
        }

        fun throwableIs401(throwable: Throwable): Boolean {
            if (throwable is HttpException && throwable.code() == 401) {
                return true
            }
            return false
        }
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = sharedPreferences
            .getString(Constants.SHARED_PREFERENCES_KEY_TOKEN, "") ?: ""

        if (token.isNotEmpty()) {
            request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", token))
                .build()
        }

        val response = chain.proceed(request)
        if (response.code() == 401) {
            setValueSafeOnBackgroundThread()
        }
        AccountApi.setIsOfflineValue(false)

        return response
    }

    private fun setValueSafeOnBackgroundThread() {
        _shouldRestart.postValue(true)
    }
}