package com.example.fair2share.activity.transactions.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty

@Suppress("UNCHECKED_CAST")
class ManagePeopleInTransactionViewModelFacotry(private val activity: ActivityDTOProperty, private val transaction: TransactionDTOProperty, private val database: Fair2ShareDatabase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ManagePeopleInTransactionViewModel::class.java) -> {
                ManagePeopleInTransactionViewModel(activity, transaction, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}