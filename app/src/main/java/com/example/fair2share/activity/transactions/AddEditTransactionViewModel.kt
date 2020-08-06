package com.example.fair2share.activity.transactions

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.database.TransactionRepository
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty

class AddEditTransactionViewModel(val activity: ActivityDTOProperty, val transaction: TransactionProperty, var isNewTransaction: Boolean, database: Fair2ShareDatabase) : ViewModel() {
    private val transactionRepository = TransactionRepository(database)

    val errorMessage: LiveData<String> = transactionRepository.errorMessage
    val navigate: LiveData<Boolean> = transactionRepository.navigate

    fun createOrUpdate(resources: Resources){
        transactionRepository.createOrUpdate(resources, isNewTransaction, activity, transaction)
    }

    fun removeTransaction(resources: Resources){
        transactionRepository.removeTransaction(resources, activity.activityId!!, transaction.transactionId!!)
    }
}