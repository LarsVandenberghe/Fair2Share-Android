package com.example.fair2share.activity.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty

@Suppress("UNCHECKED_CAST")
class AddEditTransactionViewModelFactory(private val activity: ActivityDTOProperty, private val transaction: TransactionProperty, val isNewTransaction: Boolean, val database: Fair2ShareDatabase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java) -> {
                AddEditTransactionViewModel(activity, transaction, isNewTransaction, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}