package com.example.fair2share.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.utils.Constants
import com.example.fair2share.viewmodels.profile.ProfileFragmentViewModel
import com.example.fair2share.viewmodels.startup.StartUpViewModel

@Suppress("UNCHECKED_CAST")
class DatabaseOnlyViewModelFactory(private val database: Fair2ShareDatabase) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartUpViewModel::class.java)) {
            return StartUpViewModel(database) as T
        } else if (modelClass.isAssignableFrom(ProfileFragmentViewModel::class.java)) {
            return ProfileFragmentViewModel(database) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
    }
}