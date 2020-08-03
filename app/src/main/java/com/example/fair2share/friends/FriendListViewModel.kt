package com.example.fair2share.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
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

class FriendListViewModel(friendsArg: List<ProfileDTOProperty>?) : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _friendRequests = MutableLiveData<List<ProfileDTOProperty>>()
    val friendRequests: LiveData<List<ProfileDTOProperty>>
        get() = _friendRequests

    private val _friends = MutableLiveData<List<ProfileDTOProperty>>()
    val friends: LiveData<List<ProfileDTOProperty>>
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
                204 -> _succes.value = true
                else -> {
                    try {
                        Utils.throwExceptionIfHttpNotSuccessful(result)
                        _succes.value = true
                    } catch (e: HttpException){
                        _errorMessage.value = Utils.formExceptionsToString(e)
                    } catch (t: Throwable){
                        _errorMessage.value = t.message
                    }
                }
            }
        }
    }
}