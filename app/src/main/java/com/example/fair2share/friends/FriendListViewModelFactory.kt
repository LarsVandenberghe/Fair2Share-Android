package com.example.fair2share.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ProfileDTOProperty

@Suppress("UNCHECKED_CAST")
class FriendListViewModelFactory(
    val profile: ProfileDTOProperty,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendListViewModel::class.java)) {
            return FriendListViewModel(profile, database) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}