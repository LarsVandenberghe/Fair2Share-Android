package com.example.fair2share.viewmodels.activity

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.formdata_models.ActivityFormProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository

class AddEditActivityFragmentViewModel(
    val activity: ActivityFormProperty,
    database: Fair2ShareDatabase
) : ViewModel() {
    private val activityRepository: IActivityRepository =
        ActivityRepository(database)

    val errorMessage: LiveData<String> = activityRepository.errorMessage
    val navigate: LiveData<Boolean> = activityRepository.navigate

    val isNewActivity: Boolean
        get() = activity.activityId == null


    fun createOrUpdate(resources: Resources) {
        activityRepository.createOrUpdate(resources, isNewActivity, activity)
    }
}