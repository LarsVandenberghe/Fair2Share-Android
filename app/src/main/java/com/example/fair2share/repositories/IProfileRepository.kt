package com.example.fair2share.repositories

import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ProfileDTOProperty

interface IProfileRepository {
    val profile: LiveData<ProfileDTOProperty>
    val friendRequests: LiveData<List<ProfileDTOProperty>>

    fun updateFromSafeArgs(profile: ProfileDTOProperty)
    suspend fun update()
    suspend fun updateOnStartUpCheckOnline()
    fun updateWithCachedProfileOnStartUp()
    suspend fun updateFriendRequestsWithApi()
    suspend fun handleFriendRequest(userId: Long, accept: Boolean)
    suspend fun addFriendByEmail(myProfileEmailAddress: String, email: String)
}