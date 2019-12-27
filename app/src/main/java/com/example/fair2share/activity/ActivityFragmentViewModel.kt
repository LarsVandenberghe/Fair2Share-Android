package com.example.fair2share.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityFragmentViewModel(private val activityId : Long):ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _activity = MutableLiveData<ActivityProperty>()
    val activity: LiveData<ActivityProperty>
        get() = _activity

    init {
        coroutineScope.launch {
            //_activity.value = ActivityApi.retrofitService.getActivity(activityId).await()
            Log.i("ActivityFragmentVM",  ActivityApi.retrofitService.getActivityTransactions(activityId).await().toString())
        }
    }
}