package com.example.fair2share.network

import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.LoginProperty
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitJsonMap = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BuildConfig.BASE_URL)
    .build()


interface AccountApiService {
    @POST("Account")
    fun login(@Body loginProperty: LoginProperty):
            Deferred<String>

//    @GET("Persoon/clienten")
//    fun getClienten():
//            Deferred<List<PersoonProperty>>
//
//    @GET("Persoon/begeleiders")
//    fun getBegeleiders():
//            Deferred<List<PersoonProperty>>
}

object AccountApi {
    val retrofitService : AccountApiService by lazy {
        retrofitJsonMap.create(AccountApiService::class.java)
    }
}