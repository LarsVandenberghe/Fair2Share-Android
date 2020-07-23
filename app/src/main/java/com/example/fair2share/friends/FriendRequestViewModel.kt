package com.example.fair2share.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.FriendRequestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.logging.Logger

class FriendRequestViewModel: ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _friends = MutableLiveData<List<ProfileProperty>>()
    val profile: LiveData<List<ProfileProperty>>
        get() = _friends

    init {
        _coroutineScope.launch {
            val getJWTDeffered = FriendRequestApi.retrofitService.getFriendRequest()
            //Log.i("FriendRequestViewModel",getJWTDeffered.await().toString() )
            _friends.value = getJWTDeffered.await()
        }
    }
}