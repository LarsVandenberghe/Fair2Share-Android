package com.example.fair2share.viewmodels.profile

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository

class ProfileFragmentViewModel(val database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository: IProfileRepository = ProfileRepository(database)
    private val activityRepository: IActivityRepository =
        ActivityRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val activityErrorMessage: LiveData<String> = activityRepository.errorMessage
    val activityDeleteSuccess: LiveData<Boolean> = activityRepository.success

    fun update(resources: Resources, profile: ProfileDTOProperty? = null) {
        if (profile != null) {
            profileRepository.updateFromSafeArgs(profile)
        } else {
            profileRepository.update(resources)
        }
    }

    fun removeActivity(resources: Resources, activity: ActivityDTOProperty) {
        activityRepository.removeActivity(resources, activity.activityId!!)
    }
}