package com.example.fair2share.profile

import android.content.res.Resources
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.fair2share.BuildConfig
import com.example.fair2share.Utils
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.ProfileRepository
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ProfileFragmentViewModel(val database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository = ProfileRepository(database)
    private val activityRepository = ActivityRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val activityErrorMessage: LiveData<String> = activityRepository.errorMessage
    val activityDeleteSuccess: LiveData<Boolean> = activityRepository.success

    fun update(resouces: Resources, profile: ProfileDTOProperty? = null){
        if (profile != null) {
            profileRepository.updateFromSafeArgs(profile)
        } else {
            profileRepository.updateProfileWithRoom()
            profileRepository.updateProfileWithApi(resouces)
        }
    }

    fun removeActivity(resources: Resources, activity: ActivityDTOProperty){
        activityRepository.removeActivity(resources, activity.activityId!!)
    }
}
