package com.example.fair2share.login

import android.util.Log
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor: Interceptor {
//    companion object {
//        fun newInstance() = AuthInterceptor()
//    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = sharedPreferences?.getString("token", "") ?: ""
        if (token.isNotEmpty()){
            request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", token))
                .build()
        }
        return chain.proceed(request)
    }
}