package com.example.fair2share.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentProfileViewModel : ViewModel() {

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _profile = MutableLiveData<ProfileProperty>()
    val profile: LiveData<ProfileProperty>
        get() = _profile

    init {
        loadProfile()
    }

    private fun loadProfile(){
        _coroutineScope.launch {
            _profile.value = ProfileApi.retrofitService.profile().await()
        }
    }

    fun getProfilePicUrl(profile : ProfileProperty): String{
        return String.format("%sProfile/image/%s", BuildConfig.BASE_URL, profile.profileId)
    }
}
