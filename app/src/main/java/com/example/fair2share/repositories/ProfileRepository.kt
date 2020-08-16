package com.example.fair2share.repositories

import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.exceptions.CustomHttpException
import com.example.fair2share.models.FriendRequestStates
import com.example.fair2share.models.database_models.ProfileDatabaseProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.FriendRequestApi
import com.example.fair2share.network.ProfileApi
import com.example.fair2share.utils.Constants
import com.example.fair2share.utils.Utils
import com.squareup.moshi.Moshi

class ProfileRepository(private val database: Fair2ShareDatabase) : IProfileRepository {

    private val sharedPreferences: SharedPreferences = AccountApi.sharedPreferences
    private val jsonAdapter = Moshi.Builder().build().adapter(ProfileDTOProperty::class.java)

    private val _profile = MutableLiveData<ProfileDTOProperty>()
    override val profile: LiveData<ProfileDTOProperty>
        get() = _profile

    private val _friendRequests = MutableLiveData<List<ProfileDTOProperty>>(emptyList())
    override val friendRequests: LiveData<List<ProfileDTOProperty>>
        get() = _friendRequests


    override fun updateFromSafeArgs(profile: ProfileDTOProperty) {
        _profile.postValue(profile)
    }

    override suspend fun update() {
        updateProfileWithRoom()
        updateProfileWithApi()
    }

    override suspend fun updateOnStartUpCheckOnline() {
        updateProfileWithApi()
    }

    override fun updateWithCachedProfileOnStartUp() {
        updateProfileWithRoom()
    }

    override suspend fun updateFriendRequestsWithApi() {
        val deferred = FriendRequestApi.retrofitService.getFriendRequest()
        _friendRequests.postValue(
            deferred.await().filter { potentialFriend ->
                potentialFriend.friendRequestState == FriendRequestStates.PENDING.ordinal
            }
        )
    }

    override suspend fun handleFriendRequest(userId: Long, accept: Boolean) {
        val getJWTDeferred =
            FriendRequestApi.retrofitService.handleFriendRequest(userId, accept)
        val result = getJWTDeferred.await()
        when (result.code()) {
            500 -> throw CustomHttpException(R.string.fragment_friendrequest_defaulterror)
            else -> {
                Utils.throwExceptionIfHttpNotSuccessful(result)
            }
        }
    }

    override suspend fun addFriendByEmail(
        myProfileEmailAddress: String,
        email: String
    ) {
        if (myProfileEmailAddress == email) {
            throw CustomHttpException(R.string.fragment_addfriend_errorsendyourself)
        } else {
            val deferred = FriendRequestApi.retrofitService.addFriendByEmail(email)
            val result = deferred.await()
            when (result.code()) {
                500 -> throw CustomHttpException(R.string.fragment_addfriend_alreadpending)
                404 -> throw CustomHttpException(R.string.fragment_addfriend_emailnotfound)
                else -> {
                    Utils.throwExceptionIfHttpNotSuccessful(result)
                }
            }
        }
    }

    private fun updateProfileWithRoom() {
        val profileId = sharedPreferences
            .getLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, 0L)

        if (profileId == 0L) {
            return
        }
        Handler(Looper.getMainLooper()).post {
            Transformations.map(database.profileDao.getProfile(profileId)) { property: ProfileDatabaseProperty? ->
                if (property != null) {
                    jsonAdapter.fromJson(property.data)
                } else {
                    null
                }
            }.observeForever { data -> if (data != null) _profile.postValue(data) }
        }
    }

    private suspend fun updateProfileWithApi() {
        val data = ProfileApi.retrofitService.profile().await()
        _profile.postValue(data)
        sharedPreferences.edit()
            .putLong(Constants.SHARED_PREFERENCES_KEY_PROFILEID, data.profileId)?.apply()

        database.profileDao.insertProfile(data.makeDatabaseModel())
    }
}