package com.example.fair2share.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FragmentProfileViewModel : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    fun profile(){
        _coroutineScope.launch {
            var profile = ProfileApi.retrofitService.profile().await()
            Log.i("FragmentProfileVM", profile)
        }
    }
}
