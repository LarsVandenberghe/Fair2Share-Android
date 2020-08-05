package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.activity.people.ManagePeopleInActivityViewModel
import com.example.fair2share.activity.summary.ActivitySummaryViewModel
import com.example.fair2share.activity.transactions.ActivityTransactionsFragmentViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import java.lang.IllegalArgumentException

class AddEditActivityFragmentViewModelFactory(private val activity : ActivityProperty, private val database: Fair2ShareDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditActivityFragmentViewModel::class.java) -> {
                AddEditActivityFragmentViewModel(activity, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}