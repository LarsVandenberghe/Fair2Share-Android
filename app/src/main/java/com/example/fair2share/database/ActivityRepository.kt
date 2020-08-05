package com.example.fair2share.database

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.ActivitySummaryDatabaseProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.dto_models.asDataModel2
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.network.ProfileApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.lang.Exception
import java.lang.StringBuilder
import java.net.ConnectException
import com.squareup.moshi.Types
import com.example.fair2share.PairAdapterFactory


class ActivityRepository(private val database: Fair2ShareDatabase) {
    private val jsonAdapter = Moshi.Builder().build().adapter(ActivityDTOProperty::class.java)

    private var stringlistTypes = Types.newParameterizedType(List::class.java, String::class.java)
    private var pairTypes = Types.newParameterizedType(Pair::class.java, ProfileDTOProperty::class.java, Double::class.javaObjectType)
    private val jsonSummaryListAdapter = Moshi.Builder().build().adapter<List<String>>(stringlistTypes)
    private val jsonSummaryPairAdapter = Moshi.Builder().add(PairAdapterFactory).build().adapter<Pair<ProfileDTOProperty, Double>>(pairTypes)

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success


    private val _activity = MutableLiveData<ActivityDTOProperty>()
    val activity: LiveData<ActivityDTOProperty>
        get() = _activity

    private val _summary = MutableLiveData<List<Pair<ProfileDTOProperty, Double>>>()
    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>>
        get() = _summary

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun removeActivity(resources: Resources, activityId: Long){
         _coroutineScope.launch {
             try {
                 val deferred = ActivityApi.retrofitService.removeActivity(activityId).await()
                 if (!deferred.isSuccessful){
                     val sb = StringBuilder()
                     val charStream = deferred.errorBody()?.charStream()
                     if (charStream != null){
                         sb.append(charStream.readText())
                     }
                     _errorMessage.postValue(sb.toString())
                 } else {
                     _success.postValue(true)
                 }
             } catch (e: ConnectException){
                 AccountApi.setIsOfflineValue(true)
                 _errorMessage.postValue(resources.getString(R.string.offline_error))
             }
         }
    }

    fun createOrUpdate(resources: Resources, isNewActivity: Boolean, activity: ActivityProperty){
        _coroutineScope.launch {
            try {
                val response = if (isNewActivity){
                    ActivityApi.retrofitService.addActivity(activity.makeDTO()).await()
                } else {
                    ActivityApi.retrofitService.updateActivity(activity.activityId!!, activity.makeDTO()).await()
                }
                Utils.throwExceptionIfHttpNotSuccessful(response)
                _navigate.value = true
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (e: HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e)
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }

    fun updateActivityWithRoom(activityId: Long){
        val profileId = sharedPreferences.getLong("profileId", 0L)
        if (profileId == 0L){
            throw Exception("ProfileID sharedPreferences not set!")
        }

        Transformations.map(database.activityDao.getActivity(profileId, activityId)){ property : ActivityDatabaseProperty? ->
            if (property != null){
                jsonAdapter.fromJson(property.data)
            } else {
                null
            }
        }.observeForever{data: ActivityDTOProperty? ->
            if (data != null) _activity.postValue(data)
        }
    }

    fun updateActivityWithApi(resources: Resources, activityId: Long){
        _coroutineScope.launch {
            try {
                val activity = ActivityApi.retrofitService.getActivityParticipants(activityId).await().makeDataModel()
                activity.transactions = ActivityApi.retrofitService.getActivityTransactions(activityId).await().asDataModel2()
                val activityDTO = activity.makeDTO()
                val profileId = sharedPreferences.getLong("profileId", 0L)
                _activity.postValue(activityDTO)
                database.activityDao.insertActivity(activityDTO.makeDatabaseModel(profileId))
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))

            } catch (t: Throwable){
                _errorMessage.postValue(t.message)
            }
        }
    }

    fun updateActivitySummaryWithRoom(activityId: Long){
        val profileId = sharedPreferences.getLong("profileId", 0L)
        if (profileId == 0L){
            throw Exception("ProfileID sharedPreferences not set!")
        }

        Transformations.map(database.summaryDao.getActivitySummary(profileId, activityId)){ property: ActivitySummaryDatabaseProperty? ->
            if (property != null){
                jsonSummaryListAdapter.fromJson(property.data)?.map {
                    jsonSummaryPairAdapter.fromJson(it)!!
                }
            } else {
                null
            }
        }.observeForever{data: List<Pair<ProfileDTOProperty, Double>>? ->
            if (data != null) _summary.postValue(data)
        }
    }

    fun updateActivitySummaryWithApi(resources: Resources, activity: ActivityDTOProperty){
        _coroutineScope.launch {
            try {
                val profileId = sharedPreferences.getLong("profileId", 0L)
                if (profileId == 0L){
                    throw Exception("ProfileID sharedPreferences not set!")
                }

                val summary = ActivityApi.retrofitService.getActivitySummary(activity.activityId!!).await().map {
                    val profile = (activity.participants as List).find {participant ->
                        participant.profileId == it.key.toLong()
                    }
                    val out : Pair<ProfileDTOProperty, Double> = Pair(
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
            } catch (e: ConnectException){
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable){
                _errorMessage.postValue(t.message)
            }
        }
    }
}