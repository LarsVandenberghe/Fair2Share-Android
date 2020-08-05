package com.example.fair2share.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.ProfileRepository
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.FriendRequestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class AddFriendViewModel(private val myProfileEmailAddress: String, database: Fair2ShareDatabase) : ViewModel() {
    private val profileRepository = ProfileRepository(database)

    val errorMessage: LiveData<String> = profileRepository.errorMessage
    val succes: LiveData<Boolean> = profileRepository.success

    fun addFriendByEmail(email: String, resources: Resources){
        profileRepository.addFriendByEmail(myProfileEmailAddress, email, resources)
    }
}