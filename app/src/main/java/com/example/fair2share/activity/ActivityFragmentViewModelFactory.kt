package com.example.fair2share.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.activity.people.ManagePeopleInActivityViewModel
import com.example.fair2share.activity.summary.ActivitySummaryViewModel
import com.example.fair2share.activity.transactions.ActivityTransactionsFragmentViewModel
import com.example.fair2share.data_models.ActivityProperty
import java.lang.IllegalArgumentException

class ActivityFragmentViewModelFactory(private val activity : ActivityProperty) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ActivityTransactionsFragmentViewModel::class.java) -> {
                ActivityTransactionsFragmentViewModel(activity) as T
            }
            modelClass.isAssignableFrom(ActivitySummaryViewModel::class.java) -> {
                ActivitySummaryViewModel(activity) as T
            }
            modelClass.isAssignableFrom(ManagePeopleInActivityViewModel::class.java) -> {
                ManagePeopleInActivityViewModel(activity) as T
            }
            modelClass.isAssignableFrom(AddEditActivityFragmentViewModel::class.java) -> {
                AddEditActivityFragmentViewModel(activity) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}