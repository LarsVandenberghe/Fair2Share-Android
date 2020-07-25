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

    //TODO: Implement stringify
    fun addFriendByEmail(email: String){
        _coroutineScope.launch {

            if (myProfileEmailAddress.equals(email)){
                _errorMessage.value = "You can't send yourself a friend request."
            } else {
                val getJWTDeffered = FriendRequestApi.retrofitService.addFriendByEmail(email)
                val result = getJWTDeffered.await()
                when (result.code()){
                    500 -> _errorMessage.value = "Something went wrong. Maybe you have a pending friend request from this email?"
                    404 -> _errorMessage.value = "Email does not exist."
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
}