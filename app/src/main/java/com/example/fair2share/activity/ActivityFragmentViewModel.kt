package com.example.fair2share.activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.ActivityProperty

class ActivityFragmentViewModel(private val activityId : Long):ViewModel() {
    private val _activity = MutableLiveData<ActivityProperty>()
    val activity: LiveData<ActivityProperty>
        get() = _activity
}