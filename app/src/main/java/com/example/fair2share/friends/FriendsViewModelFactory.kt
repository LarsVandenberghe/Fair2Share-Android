package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.login.LoginViewModel

class FriendsViewModelFactory(val friends: List<ProfileProperty>?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendsViewModel::class.java)){
            return FriendsViewModel(friends) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}