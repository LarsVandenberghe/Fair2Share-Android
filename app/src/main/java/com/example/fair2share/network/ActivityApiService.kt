package com.example.fair2share.network

import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.login.AuthInterceptor
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.*

private val httpClient = OkHttpClient.Builder()
    .addInterceptor(AuthInterceptor()).build()

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofitJsonMap =
    Retrofit.Builder()
        .client(httpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        //.addConverterFactory(ScalarsConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BuildConfig.BASE_URL)
        .build()


interface ActvityApiService {
    @GET("Activity/{id}")
    fun getActivity(@Path("id") id : Long):
            Deferred<ActivityProperty>

    @GET("Activity/{id}/transactions")
    fun getActivityTransactions(@Path("id") id : Long):
            Deferred<List<TransactionProperty>>

    @GET("Activity/{id}/summary")
    fun getActivitySummary(@Path("id") id : Long):
            Deferred<Map<String, Double>>

    @GET("Activity/{id}/participants")
    fun getActivityParticipants(@Path("id") id : Long):
            Deferred<ActivityProperty>

    @DELETE("Activity/{id}")
    fun removeActivity(@Path("id") id: Long):
            Deferred<Response<Unit>>

    @POST("Activity")
    fun addActivity(@Body activityProperty: ActivityProperty):
            Deferred<Response<Unit>>
}

object ActivityApi {
    val retrofitService : ActvityApiService by lazy {
        retrofitJsonMap.create(ActvityApiService::class.java)
    }
}