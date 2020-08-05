package com.example.fair2share.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.database.ActivityRepository
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.ProfileRepository
import com.example.fair2share.models.data_models.FriendRequestStates
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.dto_models.asDataModel
import com.example.fair2share.network.FriendRequestApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class FriendListViewModel(var profileArg: ProfileDTOProperty, database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository = ProfileRepository(database)

    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile
    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val success: LiveData<Boolean> = profileRepository.success
    val friendRequests: LiveData<List<ProfileDTOProperty>> = profileRepository.friendRequests

    init {
        if (profileArg.friends != null){
            profileRepository.updateFromSafeArgs(profileArg)
        }
        profile.observeForever{
            profileArg = it
        }
    }

    fun update(resources: Resources){
        profileRepository.updateProfileWithApi(resources)
        profileRepository.updateFriendRequestsWithApi(resources)
    }

    fun handleFriendRequest(userId: Long, accept: Boolean, resources: Resources){
        profileRepository.handleFriendRequest(userId, accept, resources)
    }
}