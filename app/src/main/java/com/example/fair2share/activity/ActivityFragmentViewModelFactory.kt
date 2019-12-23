package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

class ActivityFragmentViewModelFactory(private val activityId : Long) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityFragmentViewModel::class.java)){
            return ActivityFragmentViewModel(activityId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}