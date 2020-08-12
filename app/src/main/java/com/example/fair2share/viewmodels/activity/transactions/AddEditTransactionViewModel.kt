package com.example.fair2share.viewmodels.activity.transactions

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty
import com.example.fair2share.repositories.ITransactionRepository
import com.example.fair2share.repositories.TransactionRepository

class AddEditTransactionViewModel(
    val activity: ActivityDTOProperty,
    val transaction: TransactionFormProperty,
    var isNewTransaction: Boolean,
    database: Fair2ShareDatabase
) : ViewModel() {
    private val transactionRepository: ITransactionRepository =
        TransactionRepository(database)

    val errorMessage: LiveData<String> = transactionRepository.errorMessage
    val navigate: LiveData<Boolean> = transactionRepository.navigate

    fun createOrUpdate(resources: Resources) {
        transactionRepository.createOrUpdate(resources, isNewTransaction, activity, transaction)
    }

    fun removeTransaction(resources: Resources) {
        transactionRepository.removeTransaction(
            resources,
            activity.activityId!!,
            transaction.transactionId!!
        )
    }
}