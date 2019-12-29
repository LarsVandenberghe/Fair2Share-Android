package com.example.fair2share.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.fair2share.BuildConfig
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.ActivityApi
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
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    fun update(){
        _coroutineScope.launch {
            _profile.value = ProfileApi.retrofitService.profile().await()
            Log.i("Lala", _profile.value.toString())
        }
    }

    fun getProfilePicUrl(profile : ProfileProperty): GlideUrl{
        return GlideUrl(String.format("%sProfile/image/%s", BuildConfig.BASE_URL, profile.profileId), LazyHeaders.Builder()
            .addHeader("Authorization", String.format("Bearer %s", AccountApi.token)).build())
    }

    fun removeActivity(activity: ActivityProperty){
        _coroutineScope.launch {
            val a = ActivityApi.retrofitService.removeActivity(activity.activityId!!).await()
            if (!a.isSuccessful){
                _errorMessage.value = a.errorBody()!!.string()
            } else {
                update()
            }
        }
    }
}
