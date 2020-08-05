package com.example.fair2share.database

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.profile.ProfileFragmentViewModel
import com.example.fair2share.startup.StartUpViewModel

class DatabaseOnlyViewModelFactory(private val database: Fair2ShareDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartUpViewModel::class.java)){
            return StartUpViewModel(database) as T
        } else if (modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java)){
            return ProfileFragmentViewModel(database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}