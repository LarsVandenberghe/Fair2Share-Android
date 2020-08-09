package com.example.fair2share.activity.summary

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository

class ActivitySummaryViewModel(var activityArg: ActivityDTOProperty, database: Fair2ShareDatabase) :
    ViewModel() {
    private val activityRepository: IActivityRepository = ActivityRepository(database)

    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>> = activityRepository.summary
    val activity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val errorMessage: LiveData<String> = activityRepository.errorMessage

    init {
        activity.observeForever {
            activityArg = it
        }
    }

    fun update(resources: Resources) {
        activityRepository.updateSummary(resources, activityArg)
    }
}
