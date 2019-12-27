package com.example.fair2share.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
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


    fun update(){
        _coroutineScope.launch {
            _profile.value = ProfileApi.retrofitService.profile().await()
        }
    }

    fun getProfilePicUrl(profile : ProfileProperty): GlideUrl{
        return GlideUrl(String.format("%sProfile/image/%s", BuildConfig.BASE_URL, profile.profileId), LazyHeaders.Builder()
            .addHeader("Authorization", String.format("Bearer %s", AccountApi.token)).build())
    }
}
