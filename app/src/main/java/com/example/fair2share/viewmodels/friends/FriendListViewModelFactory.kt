package com.example.fair2share.viewmodels.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository
import com.example.fair2share.utils.Constants

@Suppress("UNCHECKED_CAST")
class FriendListViewModelFactory(
    val profile: ProfileDTOProperty,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FriendListViewModel::class.java)) {
            val profileRepository: IProfileRepository = ProfileRepository(database)
            return FriendListViewModel(profile, profileRepository) as T
        }
        throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
    }
}