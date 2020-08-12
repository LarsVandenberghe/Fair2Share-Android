package com.example.fair2share.viewmodels.activity.transactions

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository

class ActivityTransactionsFragmentViewModel(
    var activityArg: ActivityDTOProperty,
    database: Fair2ShareDatabase
) : ViewModel() {
    private val activityRepository: IActivityRepository =
        ActivityRepository(database)

    val activity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val errorMessage: LiveData<String> = activityRepository.errorMessage
    val navigate: LiveData<Boolean> = activityRepository.navigate

    init {
        activity.observeForever {
            activityArg = it
        }
    }

    fun update(resources: Resources) {
        activityRepository.update(resources, activityArg.activityId!!)
    }


    fun removeActivity(resources: Resources) {
        activityRepository.removeActivity(resources, activityArg.activityId!!)
    }
}