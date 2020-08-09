package com.example.fair2share.repositories

import android.content.SharedPreferences
import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.FriendRequestStates
import com.example.fair2share.models.database_models.ProfileDatabaseProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.network.FriendRequestApi
import com.example.fair2share.network.ProfileApi
import com.example.fair2share.util.Utils
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class ProfileRepository(private val database: Fair2ShareDatabase) : IProfileRepository {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val sharedPreferences: SharedPreferences = AccountApi.sharedPreferences
    private val jsonAdapter = Moshi.Builder().build().adapter(ProfileDTOProperty::class.java)

    private val _errorMessage = MutableLiveData<String>()
    override val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _shouldRelog = MutableLiveData<Boolean>()
    override val shouldRelog: LiveData<Boolean>
        get() = _shouldRelog

    private val _profile = MutableLiveData<ProfileDTOProperty>()
    override val profile: LiveData<ProfileDTOProperty>
        get() = _profile

    private val _friendRequests = MutableLiveData<List<ProfileDTOProperty>>(emptyList())
    override val friendRequests: LiveData<List<ProfileDTOProperty>>
        get() = _friendRequests

    private val _success = MutableLiveData<Boolean>()
    override val success: LiveData<Boolean>
        get() = _success

    override fun updateFromSafeArgs(profile: ProfileDTOProperty) {
        _profile.postValue(profile)
    }

    override fun update(resouces: Resources) {
        updateProfileWithRoom()
        updateProfileWithApi(resouces)
    }

    override fun updateOnStartUpCheckOnline(resouces: Resources) {
        updateProfileWithApi(resouces)
    }

    override fun updateWithCachedProfileOnStartUp() {
        updateProfileWithRoom()
    }

    override fun updateFriendRequestsWithApi(resources: Resources) {
        _coroutineScope.launch {
            try {
                val deffered = FriendRequestApi.retrofitService.getFriendRequest()
                _friendRequests.postValue(
                    deffered.await().filter { potentialFriend ->
                        potentialFriend.friendRequestState == FriendRequestStates.PENDING.ordinal
                    }
                )
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                if (AuthInterceptor.throwableIs401(t)) {
                    _errorMessage.postValue(resources.getString(R.string.fragment_startup_tokenexipred))
                    _shouldRelog.postValue(true)
                } else {
                    _errorMessage.postValue(t.message)
                }
            }
        }
    }

    override fun handleFriendRequest(userId: Long, accept: Boolean, resources: Resources) {
        _coroutineScope.launch {
            try {
                val getJWTDeffered =
                    FriendRequestApi.retrofitService.handleFriendRequest(userId, accept)
                val result = getJWTDeffered.await()
                when (result.code()) {
                    500 -> _errorMessage.postValue(resources.getString(R.string.fragment_friendrequest_defaulterror))
                    204 -> _success.postValue(true)
                    else -> {
                        try {
                            Utils.throwExceptionIfHttpNotSuccessful(result)
                            _success.postValue(true)
                        } catch (e: HttpException) {
                            _errorMessage.postValue(Utils.formExceptionsToString(e))
                        } catch (t: Throwable) {
                            _errorMessage.postValue(t.message)
                        }
                    }
                }
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            }
        }
    }

    override fun addFriendByEmail(
        myProfileEmailAddress: String,
        email: String,
        resources: Resources
    ) {
        _coroutineScope.launch {
            try {
                if (myProfileEmailAddress.equals(email)) {
                    _errorMessage.postValue(resources.getString(R.string.fragment_addfriend_errorsendyourself))
                } else {
                    val deffered = FriendRequestApi.retrofitService.addFriendByEmail(email)
                    val result = deffered.await()
                    when (result.code()) {
                        500 -> _errorMessage.postValue(resources.getString(R.string.fragment_addfriend_alreadpending))
                        404 -> _errorMessage.postValue(resources.getString(R.string.fragment_addfriend_emailnotfound))
                        else -> {
                            try {
                                Utils.throwExceptionIfHttpNotSuccessful(result)
                                _success.postValue(true)
                            } catch (e: HttpException) {
                                _errorMessage.postValue(Utils.formExceptionsToString(e))
                            } catch (t: Throwable) {
                                _errorMessage.postValue(t.message)
                            }
                        }
                    }
                }
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            }
        }
    }

    private fun updateProfileWithRoom() {
        val profileId = sharedPreferences.getLong("profileId", 0L)
        if (profileId == 0L) {
            return
        }

        Transformations.map(database.profileDao.getProfile(profileId)) { property: ProfileDatabaseProperty? ->
            if (property != null) {
                jsonAdapter.fromJson(property.data)
            } else {
                null
            }
        }.observeForever { data -> if (data != null) _profile.postValue(data) }
    }

    private fun updateProfileWithApi(resources: Resources) {
        _coroutineScope.launch {
            try {
                val data = ProfileApi.retrofitService.profile().await()
                _profile.postValue(data)
                sharedPreferences.edit().putLong("profileId", data.profileId)?.apply()
                database.profileDao.insertProfile(data.makeDatabaseModel())
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                if (AuthInterceptor.throwableIs401(t)) {
                    _errorMessage.postValue(resources.getString(R.string.fragment_startup_tokenexipred))
                    _shouldRelog.postValue(true)
                } else {
                    _errorMessage.postValue(t.message)
                }
            }
        }
    }
}