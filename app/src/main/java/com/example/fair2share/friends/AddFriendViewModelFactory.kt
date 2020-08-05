package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase

class AddFriendViewModelFactory(private val myProfileEmailAddress: String, private val database: Fair2ShareDatabase): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)){
            return AddFriendViewModel(myProfileEmailAddress, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}