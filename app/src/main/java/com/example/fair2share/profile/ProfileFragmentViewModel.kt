package com.example.fair2share.profile

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.ProfileRepository
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty

class ProfileFragmentViewModel(val database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository = ProfileRepository(database)
    private val activityRepository = ActivityRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val activityErrorMessage: LiveData<String> = activityRepository.errorMessage
    val activityDeleteSuccess: LiveData<Boolean> = activityRepository.success

    fun update(resouces: Resources, profile: ProfileDTOProperty? = null) {
        if (profile != null) {
            profileRepository.updateFromSafeArgs(profile)
        } else {
            profileRepository.update(resouces)
        }
    }

    fun removeActivity(resources: Resources, activity: ActivityDTOProperty) {
        activityRepository.removeActivity(resources, activity.activityId!!)
    }
}
