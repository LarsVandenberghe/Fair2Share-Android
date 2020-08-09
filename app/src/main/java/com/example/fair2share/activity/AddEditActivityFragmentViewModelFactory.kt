package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.formdata_models.ActivityFormProperty

@Suppress("UNCHECKED_CAST")
class AddEditActivityFragmentViewModelFactory(
    private val activity: ActivityFormProperty,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditActivityFragmentViewModel::class.java) -> {
                AddEditActivityFragmentViewModel(activity, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}