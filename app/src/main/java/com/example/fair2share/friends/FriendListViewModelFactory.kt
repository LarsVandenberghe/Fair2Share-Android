package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.data_models.ProfileProperty

class FriendListViewModelFactory(val friends: List<ProfileProperty>?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendListViewModel::class.java)){
            return FriendListViewModel(friends) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}