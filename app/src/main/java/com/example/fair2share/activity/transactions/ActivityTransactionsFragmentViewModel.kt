package com.example.fair2share.activity.transactions

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.*
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityTransactionsFragmentViewModel(var activityArg : ActivityDTOProperty, database: Fair2ShareDatabase):ViewModel() {
    private val activityRepository = ActivityRepository(database)

    val activity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val errorMessage: LiveData<String> = activityRepository.errorMessage
    val navigate: LiveData<Boolean> = activityRepository.navigate

    init {
        activity.observeForever{
            activityArg = it
        }
    }

    fun update(resources: Resources){
        activityRepository.updateActivityWithRoom(activityArg.activityId!!)
        activityRepository.updateActivityWithApi(resources, activityArg.activityId!!)
    }


    fun removeActivity(resources: Resources){
        activityRepository.removeActivity(resources, activityArg.activityId!!)
    }
}