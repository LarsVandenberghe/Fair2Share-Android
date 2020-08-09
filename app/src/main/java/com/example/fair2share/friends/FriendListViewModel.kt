package com.example.fair2share.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository

class FriendListViewModel(
    private var profileArg: ProfileDTOProperty,
    database: Fair2ShareDatabase
) : ViewModel() {
    private val profileRepository: IProfileRepository = ProfileRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val success: LiveData<Boolean> = profileRepository.success
    val friendRequests: LiveData<List<ProfileDTOProperty>> = profileRepository.friendRequests

    init {
        if (profileArg.friends != null) {
            profileRepository.updateFromSafeArgs(profileArg)
        }
        profile.observeForever {
            profileArg = it
        }
    }

    fun update(resources: Resources) {
        profileRepository.update(resources)
        profileRepository.updateFriendRequestsWithApi(resources)
    }

    fun handleFriendRequest(userId: Long, accept: Boolean, resources: Resources) {
        profileRepository.handleFriendRequest(userId, accept, resources)
    }
}