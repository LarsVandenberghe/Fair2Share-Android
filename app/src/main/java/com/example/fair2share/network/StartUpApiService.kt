package com.example.fair2share.network

import com.example.fair2share.BuildConfig
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(StartUpAuthIntercaptor()).build()

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


interface StartUpApiService {
    @GET("Profile")
    fun profile():
            Deferred<ProfileDTOProperty>
}

object StartUpApi {
    val retrofitService : StartUpApiService by lazy {
        retrofitJsonMap.create(StartUpApiService::class.java)
    }
}