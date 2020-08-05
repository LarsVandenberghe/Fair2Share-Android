package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.activity.people.ManagePeopleInActivityViewModel
import com.example.fair2share.activity.summary.ActivitySummaryViewModel
import com.example.fair2share.activity.transactions.ActivityTransactionsFragmentViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import java.lang.IllegalArgumentException

class ActivityFragmentViewModelFactory(private val activity : ActivityDTOProperty, private val database: Fair2ShareDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ActivityTransactionsFragmentViewModel::class.java) -> {
                ActivityTransactionsFragmentViewModel(activity, database) as T
            }
            modelClass.isAssignableFrom(ActivitySummaryViewModel::class.java) -> {
                ActivitySummaryViewModel(activity, database) as T
            }
            modelClass.isAssignableFrom(ManagePeopleInActivityViewModel::class.java) -> {
                ManagePeopleInActivityViewModel(activity, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}