package com.example.fair2share.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.example.fair2share.MainActivity
import com.example.fair2share.LoginActivity
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

            val intent = Intent(it.baseContext, LoginActivity::class.java)
            intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            mainActTemp.startActivity(intent)
            mainActTemp.finish()
            Runtime.getRuntime().exit(0)
        }
    }
}