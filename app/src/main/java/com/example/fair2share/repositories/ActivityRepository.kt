package com.example.fair2share.repositories

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.ActivitySummaryDatabaseProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.dto_models.asFormDataModel2
import com.example.fair2share.models.formdata_models.ActivityFormProperty
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.utils.Constants
import com.example.fair2share.utils.PairAdapterFactory
import com.example.fair2share.utils.Utils
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class ActivityRepository(private val database: Fair2ShareDatabase) : IActivityRepository {
    private val jsonAdapter = Moshi.Builder().build().adapter(ActivityDTOProperty::class.java)

    private var stringlistTypes = Types.newParameterizedType(List::class.java, String::class.java)
    private var pairTypes = Types.newParameterizedType(
        Pair::class.java,
        ProfileDTOProperty::class.java,
        Double::class.javaObjectType
    )
    private val jsonSummaryListAdapter =
        Moshi.Builder().build().adapter<List<String>>(stringlistTypes)
    private val jsonSummaryPairAdapter = Moshi.Builder().add(PairAdapterFactory).build()
        .adapter<Pair<ProfileDTOProperty, Double>>(pairTypes)

    private val _activity = MutableLiveData<ActivityDTOProperty>()
    override val activity: LiveData<ActivityDTOProperty>
        get() = _activity

    private val _summary = MutableLiveData<List<Pair<ProfileDTOProperty, Double>>>()
    override val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>>
        get() = _summary

    override suspend fun update(activityId: Long) {
        updateActivityWithRoom(activityId)
        updateActivityWithApi(activityId)
    }

    override suspend fun updateSummary(activity: ActivityDTOProperty) {
        updateActivitySummaryWithRoom(activity.activityId!!)
        updateActivitySummaryWithApi(activity)
    }

    override suspend fun removeActivity(activityId: Long) {
        val deferred = ActivityApi.retrofitService.removeActivity(activityId).await()
        if (!deferred.isSuccessful) {
            val sb = StringBuilder()
            val charStream = deferred.errorBody()?.charStream()
            if (charStream != null) {
                sb.append(charStream.readText())
            }
            throw Exception(sb.toString())
        }
    }

    override suspend fun createOrUpdate(
        isNewActivity: Boolean,
        activity: ActivityFormProperty
    ) {
        val response = if (isNewActivity) {
            ActivityApi.retrofitService.addActivity(activity.makeDTO()).await()
        } else {
            ActivityApi.retrofitService.updateActivity(
                activity.activityId!!,
                activity.makeDTO()
            ).await()
        }
        Utils.throwExceptionIfHttpNotSuccessful(response)
    }

    override suspend fun postActivityParticipants(
        activityId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    ) {
        if (toBeRemoved.isNotEmpty()) {
            val result = ActivityApi.retrofitService.removeActivityParticipants(
                activityId,
                toBeRemoved
            ).await()
            Utils.throwExceptionIfHttpNotSuccessful(result)
        }
        if (toBeAdded.isNotEmpty()) {
            val result =
                ActivityApi.retrofitService.addActivityParticipants(activityId, toBeAdded)
                    .await()
            Utils.throwExceptionIfHttpNotSuccessful(result)
        }
    }


    private fun updateActivityWithRoom(activityId: Long) {
        val profileId = sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)
        if (profileId == 0L) {
            throw Exception(Constants.SHARED_PREFERENCES_PROFILEID_NOT_SET)
        }
        Handler(Looper.getMainLooper()).post {
            Transformations.map(
                database.activityDao.getActivity(
                    profileId,
                    activityId
                )
            ) { property: ActivityDatabaseProperty? ->
                if (property != null) {
                    jsonAdapter.fromJson(property.data)
                } else {
                    null
                }
            }.observeForever { data: ActivityDTOProperty? ->
                if (data != null) _activity.postValue(data)
            }
        }
    }

    private suspend fun updateActivityWithApi(activityId: Long) {
        val activity =
            ActivityApi.retrofitService.getActivityParticipants(activityId).await()
                .makeFormDataModel()
        activity.transactions =
            ActivityApi.retrofitService.getActivityTransactions(activityId).await()
                .asFormDataModel2()
        val activityDTO = activity.makeDTO()
        val profileId = sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)

        _activity.postValue(activityDTO)
        database.activityDao.insertActivity(activityDTO.makeDatabaseModel(profileId))
    }

    private fun updateActivitySummaryWithRoom(activityId: Long) {
        val profileId = sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)

        if (profileId == 0L) {
            throw Exception(Constants.SHARED_PREFERENCES_PROFILEID_NOT_SET)
        }
        Handler(Looper.getMainLooper()).post {
            Transformations.map(
                database.summaryDao.getActivitySummary(
                    profileId,
                    activityId
                )
            ) { property: ActivitySummaryDatabaseProperty? ->
                if (property != null) {
                    jsonSummaryListAdapter.fromJson(property.data)?.map {
                        jsonSummaryPairAdapter.fromJson(it)!!
                    }
                } else {
                    null
                }
            }.observeForever { data: List<Pair<ProfileDTOProperty, Double>>? ->
                if (data != null) _summary.postValue(data)
            }
        }
    }

    private suspend fun updateActivitySummaryWithApi(activity: ActivityDTOProperty) {
        val profileId = sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)
        if (profileId == 0L) {
            throw Exception(Constants.SHARED_PREFERENCES_PROFILEID_NOT_SET)
        }

        val summary =
            ActivityApi.retrofitService.getActivitySummary(activity.activityId!!).await()
                .map {
                    val profile = (activity.participants as List).find { participant ->
                        participant.profileId == it.key.toLong()
                    }
                    val out: Pair<ProfileDTOProperty, Double> = Pair(
                        first = profile!!, second = it.value
                    )
                    out
                }

        _summary.postValue(summary)
        val jsonData = jsonSummaryListAdapter.toJson(summary.map {
            jsonSummaryPairAdapter.toJson(it)
        })

        database.summaryDao.insertActivitySummary(
            ActivitySummaryDatabaseProperty(profileId, activity.activityId, jsonData)
        )
    }
}