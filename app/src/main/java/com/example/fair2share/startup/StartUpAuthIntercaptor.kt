package com.example.fair2share.startup

import com.example.fair2share.network.AccountApi
import okhttp3.Interceptor
import okhttp3.Response

class StartUpAuthIntercaptor: Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        val token = AccountApi.sharedPreferences?.getString("token", "") ?: ""
        if (token.isNotEmpty()){
            request = request.newBuilder()
                .addHeader("Authorization", String.format("Bearer %s", token))
                .build()
        }

        return chain.proceed(request)
    }
}