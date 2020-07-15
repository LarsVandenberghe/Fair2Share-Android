package com.example.fair2share.network

import android.content.SharedPreferences
import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.login.AuthInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor()).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitJsonMap =
    Retrofit.Builder()
    .client(httpClient)
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BuildConfig.BASE_URL)
    .build()


interface AccountApiService {
    @POST("Account")
    fun login(@Body loginProperty: LoginProperty):
            Deferred<String>


}

object AccountApi {
    val retrofitService : AccountApiService by lazy {
        retrofitJsonMap.create(AccountApiService::class.java)
    }
    var sharedPreferences : SharedPreferences? = null
}