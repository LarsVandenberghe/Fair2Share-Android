package com.example.fair2share.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.fair2share.BuildConfig
import com.example.fair2share.Utils
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class ProfileFragmentViewModel : ViewModel() {

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    private val _profile = MutableLiveData<ProfileProperty>()
    val profile: LiveData<ProfileProperty>
        get() = _profile
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun update(profile: ProfileProperty? = null){
        if (profile != null) {
            _profile.value = profile
        } else {
            _coroutineScope.launch {
                _profile.value = ProfileApi.retrofitService.profile().await()
            }
        }
    }

    fun getProfilePicUrl(profile : ProfileProperty): GlideUrl{
        val token = sharedPreferences?.getString("token", "") ?: ""
        return GlideUrl(String.format("%sProfile/image/%s", BuildConfig.BASE_URL, profile.profileId), LazyHeaders.Builder()
            .addHeader("Authorization", String.format("Bearer %s", token)).build())
    }

    fun removeActivity(activity: ActivityProperty){
        _coroutineScope.launch {
            val a = ActivityApi.retrofitService.removeActivity(activity.activityId!!).await()
            if (!a.isSuccessful){
                val sb = StringBuilder()
                val charStream = a.errorBody()?.charStream()
                if (charStream != null){
                    sb.append(charStream.readText())
                }
                _errorMessage.value = sb.toString()
            } else {
                update()
            }
        }
    }
}