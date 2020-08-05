package com.example.fair2share.database

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.ActivityApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import java.lang.StringBuilder
import java.net.ConnectException

class ActivityRepository(private val database: Fair2ShareDatabase) {
    private val jsonAdapter = Moshi.Builder().build().adapter(ActivityDTOProperty::class.java)
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

    fun removeActivity(resources: Resources, activityId: Long){
         _coroutineScope.launch {
             try {
                 val a = ActivityApi.retrofitService.removeActivity(activityId).await()
                 if (!a.isSuccessful){
                     val sb = StringBuilder()
                     val charStream = a.errorBody()?.charStream()
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

//    fun getActivity(profileId: Long, activityId: Long): LiveData<ActivityDTOProperty> {
//        val data = database.activityDao.getActivity(profileId, activityId)
//        return Transformations.map(data){
//            jsonAdapter.fromJson(it.data)
//        }
//    }
//
//    suspend fun refreshActivity(profileId: Long, activityId: Long) {
//        withContext(Dispatchers.IO){
//            val data = ActivityApi.retrofitService.getActivity(activityId).await().makeDatabaseModel(profileId)
//            database.activityDao.insertActivity(data)
//        }
//    }
}