package com.example.fair2share.activity

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response

class AddEditActivityFragmentViewModel(val activity: ActivityProperty, database: Fair2ShareDatabase): ViewModel() {
    private val activityRepository = ActivityRepository(database)

    val errorMessage: LiveData<String> = activityRepository.errorMessage
    val navigate: LiveData<Boolean> = activityRepository.navigate

    val isNewActivity: Boolean
        get() = activity.activityId == null


    fun createOrUpdate(resources: Resources){
        activityRepository.createOrUpdate(resources, isNewActivity, activity)
    }
}