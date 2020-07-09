package com.example.fair2share.startup

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StartUpViewModelFactory(private val sharedPreferences: SharedPreferences): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StartUpViewModel::class.java)){
            return StartUpViewModel(sharedPreferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}