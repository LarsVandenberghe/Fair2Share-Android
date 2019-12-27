package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.data_models.ActivityProperty
import java.lang.IllegalArgumentException

class ActivityFragmentViewModelFactory(private val activity : ActivityProperty) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivityFragmentViewModel::class.java)){
            return ActivityFragmentViewModel(activity) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}