package com.example.fair2share.repositories

import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.formdata_models.ActivityFormProperty

interface IActivityRepository {
    val activity: LiveData<ActivityDTOProperty>
    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>>

    suspend fun update(activityId: Long)

    suspend fun updateSummary(activity: ActivityDTOProperty)

    suspend fun removeActivity(activityId: Long)

    suspend fun createOrUpdate(isNewActivity: Boolean, activity: ActivityFormProperty)

    suspend fun postActivityParticipants(
        activityId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    )
}