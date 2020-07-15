package com.example.fair2share.login

import android.content.Intent
import com.example.fair2share.MainActivity
import com.example.fair2share.StartUpActivity
import com.example.fair2share.network.AccountApi.sharedPreferences
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.HttpException


class AuthInterceptor: Interceptor {
    companion object {
        fun throwableIs401(throwable: Throwable) : Boolean {
            if (throwable is HttpException && throwable.code() == 401){
                return true
            }
            return false
        }

        var mainActivity: MainActivity? = null
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
            handleIf401()
        }
        return response
    }

    private fun handleIf401() {
        mainActivity?.let {
            val mainActTemp = it
            mainActivity = null

            val intent = Intent(it.baseContext, StartUpActivity::class.java)
            mainActTemp.startActivity(intent)
            mainActTemp.finish()
            Runtime.getRuntime().exit(0)
        }
    }
}