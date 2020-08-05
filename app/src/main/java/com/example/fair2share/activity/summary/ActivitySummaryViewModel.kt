package com.example.fair2share.activity.summary

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivitySummaryViewModel(var activityArg : ActivityDTOProperty, database: Fair2ShareDatabase) : ViewModel() {
    private val activityRepository = ActivityRepository(database)

    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>> = activityRepository.summary
    val activity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val errorMessage: LiveData<String> = activityRepository.errorMessage

    init {
        activity.observeForever {
            activityArg = it
        }
    }

    fun update(resources: Resources){
        activityRepository.updateActivitySummaryWithRoom(activityArg.activityId!!)
        activityRepository.updateActivitySummaryWithApi(resources, activityArg)
    }
}
