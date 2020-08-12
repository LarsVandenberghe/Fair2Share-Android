package com.example.fair2share.network

import com.example.fair2share.BuildConfig
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
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
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .baseUrl(BuildConfig.BASE_URL)
        .build()


interface ActivityApiService {
    @GET("Activity/{id}")
    fun getActivity(@Path("id") id: Long):
            Deferred<ActivityDTOProperty>

    @GET("Activity/{id}/transactions")
    fun getActivityTransactions(@Path("id") id: Long):
            Deferred<List<TransactionDTOProperty>>

    @GET("Activity/{id}/summary")
    fun getActivitySummary(@Path("id") id: Long):
            Deferred<Map<String, Double>>

    @GET("Activity/{id}/participants")
    fun getActivityParticipants(@Path("id") id: Long):
            Deferred<ActivityDTOProperty>

    @DELETE("Activity/{id}")
    fun removeActivity(@Path("id") id: Long):
            Deferred<Response<Unit>>

    @POST("Activity")
    fun addActivity(@Body activityProperty: ActivityDTOProperty):
            Deferred<Response<Unit>>

    @PUT("Activity/{id}")
    fun updateActivity(@Path("id") activityId: Long, @Body activity: ActivityDTOProperty):
            Deferred<Response<Unit>>

    @POST("Activity/{id}/participants")
    fun addActivityParticipants(@Path("id") id: Long, @Body idList: List<Long>):
            Deferred<Response<Unit>>

    @DELETE("Activity/{id}/participants")
    fun removeActivityParticipants(@Path("id") id: Long, @Query("friend_ids") idList: List<Long>):
            Deferred<Response<Unit>>

    @POST("Activity/{id}/transactions")
    fun addTransaction(@Path("id") activityId: Long, @Body transaction: TransactionDTOProperty):
            Deferred<Response<Long>>

    @PUT("Activity/{id}/transactions/{transactionId}")
    fun updateTransaction(@Path("id") activityId: Long, @Path("transactionId") transactionId: Long, @Body transaction: TransactionDTOProperty):
            Deferred<Response<Unit>>

    @GET("Activity/{id}/transactions/{transactionId}")
    fun getActivityTransactionById(@Path("id") activityId: Long, @Path("transactionId") transactionId: Long):
            Deferred<TransactionDTOProperty>

    @DELETE("Activity/{id}/transactions/{transactionId}")
    fun removeTransaction(@Path("id") activityId: Long, @Path("transactionId") transactionId: Long):
            Deferred<Response<Unit>>

    @POST("Activity/{id}/transactions/{transactionId}/participants")
    fun addTransactionParticipants(@Path("id") activityId: Long, @Path("transactionId") transactionId: Long, @Body idList: List<Long>):
            Deferred<Response<Unit>>

    @DELETE("Activity/{id}/transactions/{transactionId}/participants")
    fun removeTransactionParticipants(
        @Path("id") activityId: Long, @Path("transactionId") transactionId: Long, @Query(
            "friend_ids"
        ) idList: List<Long>
    ):
            Deferred<Response<Unit>>
}

object ActivityApi {
    val retrofitService: ActivityApiService by lazy {
        retrofitJsonMap.create(ActivityApiService::class.java)
    }
}