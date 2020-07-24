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

class FriendsViewModel(friendsArg: List<ProfileProperty>?) : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _friendRequests = MutableLiveData<List<ProfileProperty>>()
    val friendRequests: LiveData<List<ProfileProperty>>
        get() = _friendRequests

    private val _friends = MutableLiveData<List<ProfileProperty>>()
    val friends: LiveData<List<ProfileProperty>>
        get() = _friends

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
            _friends.value = getJWTDeffered.await().friends
        }
    }
}