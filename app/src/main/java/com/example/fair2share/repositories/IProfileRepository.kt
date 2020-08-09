package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ProfileDTOProperty

interface IProfileRepository {
    val errorMessage: LiveData<String>
    val shouldRelog: LiveData<Boolean>
    val profile: LiveData<ProfileDTOProperty>
    val friendRequests: LiveData<List<ProfileDTOProperty>>
    val success: LiveData<Boolean>

    fun updateFromSafeArgs(profile: ProfileDTOProperty)
    fun update(resouces: Resources)
    fun updateOnStartUpCheckOnline(resouces: Resources)
    fun updateWithCachedProfileOnStartUp()
    fun updateFriendRequestsWithApi(resources: Resources)
    fun handleFriendRequest(userId: Long, accept: Boolean, resources: Resources)
    fun addFriendByEmail(myProfileEmailAddress: String, email: String, resources: Resources)
}