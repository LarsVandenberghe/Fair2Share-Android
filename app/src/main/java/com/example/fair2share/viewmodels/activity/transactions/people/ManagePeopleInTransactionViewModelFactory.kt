package com.example.fair2share.viewmodels.activity.transactions.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.repositories.ITransactionRepository
import com.example.fair2share.repositories.TransactionRepository
import com.example.fair2share.utils.Constants

@Suppress("UNCHECKED_CAST")
class ManagePeopleInTransactionViewModelFactory(
    private val activity: ActivityDTOProperty,
    private val transaction: TransactionDTOProperty,
    private val database: Fair2ShareDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ManagePeopleInTransactionViewModel::class.java) -> {
                val activityRepository: IActivityRepository = ActivityRepository(database)
                val transactionRepository: ITransactionRepository = TransactionRepository(database)
                ManagePeopleInTransactionViewModel(
                    activity,
                    transaction,
                    activityRepository,
                    transactionRepository
                ) as T
            }
            else -> throw IllegalArgumentException(Constants.UNKNOWN_VIEWMODEL)
        }
    }
}