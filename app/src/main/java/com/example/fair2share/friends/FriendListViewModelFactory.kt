package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty

class FriendListViewModelFactory(val friends: List<ProfileDTOProperty>?): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendListViewModel::class.java)){
            return FriendListViewModel(friends) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}