package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddFriendViewModelFactory(val myProfileEmailAddress: String): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)){
            return AddFriendViewModel(myProfileEmailAddress) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}