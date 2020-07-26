package com.example.fair2share.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.FriendRequestStates
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.FriendRequestApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.logging.Logger

class FriendListViewModel(friendsArg: List<ProfileProperty>?) : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _friendRequests = MutableLiveData<List<ProfileProperty>>()
    val friendRequests: LiveData<List<ProfileProperty>>
        get() = _friendRequests

    private val _friends = MutableLiveData<List<ProfileProperty>>()
    val friends: LiveData<List<ProfileProperty>>
        get() = _friends

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _succes = MutableLiveData<Boolean>()
    val succes: LiveData<Boolean>
        get() = _succes

    lateinit var myProfileEmailAddress: String

    init {
        if (friendsArg != null){
            _friends.value = friendsArg
        }
        update()
    }

    fun update(){
        _coroutineScope.launch {
            val getJWTDeffered = FriendRequestApi.retrofitService.getFriendRequest()
            _friendRequests.value = getJWTDeffered.await().filter { potentialFriend ->
                potentialFriend.friendRequestState == FriendRequestStates.PENDING.ordinal
            }
        }

        _coroutineScope.launch {
            val getJWTDeffered = ProfileApi.retrofitService.profile()
            val profile = getJWTDeffered.await()
            _friends.value = profile.friends
            myProfileEmailAddress = profile.email!!
        }
    }

    fun handleFriendRequest(userId: Long, accept: Boolean){
        _coroutineScope.launch {
            val getJWTDeffered = FriendRequestApi.retrofitService.handleFriendRequest(userId, accept)
            val result = getJWTDeffered.await()
            when (result.code()){
                500 -> _errorMessage.value = "Something went wrong."
                400 -> {
                    if (result.errorBody() != null){
                        _errorMessage.value = result.errorBody()!!.string()
                    } else {
                        _errorMessage.value = "Something went wrong"
                    }
                }
                204 -> _succes.value = true
                else -> _errorMessage.value = String.format("(%d): %s", result.code(), result.message())
            }
        }
    }
}