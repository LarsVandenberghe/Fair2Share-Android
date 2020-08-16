package com.example.fair2share.viewmodels.activity.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty
import com.example.fair2share.repositories.ITransactionRepository
import com.example.fair2share.repositories.TransactionRepository
import com.example.fair2share.utils.Constants

@Suppress("UNCHECKED_CAST")
class AddEditTransactionViewModelFactory(
    private val activity: ActivityDTOProperty,
    private val transaction: TransactionFormProperty,
    private val isNewTransaction: Boolean,
    val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java) -> {
                val transactionRepository: ITransactionRepository = TransactionRepository(database)
                AddEditTransactionViewModel(activity, transaction, isNewTransaction, transactionRepository) as T
            }
            else -> throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
        }
    }

}