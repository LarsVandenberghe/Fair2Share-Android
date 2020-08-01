package com.example.fair2share.activity.transactions.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.activity.transactions.AddEditTransactionViewModel
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.TransactionProperty
import java.lang.IllegalArgumentException

class ManagePeopleInTransactionViewModelFacotry(private val activity: ActivityProperty, private val transaction: TransactionProperty) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ManagePeopleInTransactionViewModel::class.java) -> {
                ManagePeopleInTransactionViewModel(activity, transaction) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}