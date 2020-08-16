package com.example.fair2share.viewmodels.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.formdata_models.ActivityFormProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.utils.Constants

@Suppress("UNCHECKED_CAST")
class AddEditActivityFragmentViewModelFactory(
    private val activity: ActivityFormProperty,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditActivityFragmentViewModel::class.java) -> {
                val activityRepository: IActivityRepository = ActivityRepository(database)
                AddEditActivityFragmentViewModel(activity, activityRepository) as T
            }
            else -> throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
        }
    }

}