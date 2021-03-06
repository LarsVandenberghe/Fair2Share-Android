package com.example.fair2share.viewmodels.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository
import com.example.fair2share.utils.Constants

@Suppress("UNCHECKED_CAST")
class AddFriendViewModelFactory(
    private val myProfileEmailAddress: String,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddFriendViewModel::class.java)) {
            val profileRepository: IProfileRepository = ProfileRepository(database)
            return AddFriendViewModel(myProfileEmailAddress, profileRepository) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
    }
}