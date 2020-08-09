package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.formdata_models.ActivityFormProperty

interface IActivityRepository {
    val errorMessage: LiveData<String>
    val success: LiveData<Boolean>
    val activity: LiveData<ActivityDTOProperty>
    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>>
    val navigate: LiveData<Boolean>
    val resetSelected: LiveData<Boolean>

    fun update(resources: Resources, activityId: Long)

    fun updateSummary(resources: Resources, activity: ActivityDTOProperty)

    fun removeActivity(resources: Resources, activityId: Long)

    fun createOrUpdate(resources: Resources, isNewActivity: Boolean, activity: ActivityFormProperty)

    fun postActivityParticipants(
        resources: Resources,
        activityId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    )
}