package com.example.fair2share.viewmodels.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository

class AddFriendViewModel(private val myProfileEmailAddress: String, database: Fair2ShareDatabase) :
    ViewModel() {
    private val profileRepository: IProfileRepository = ProfileRepository(database)

    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val success: LiveData<Boolean> = profileRepository.success

    fun addFriendByEmail(email: String, resources: Resources) {
        profileRepository.addFriendByEmail(myProfileEmailAddress, email, resources)
    }
}