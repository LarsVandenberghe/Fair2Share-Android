package com.example.fair2share.repositories

import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty

interface ITransactionRepository {
    val transaction: LiveData<TransactionDTOProperty>

    suspend fun update(activityId: Long, transactionId: Long)
    suspend fun createOrUpdate(
        isNewTransaction: Boolean,
        activity: ActivityDTOProperty,
        transaction: TransactionFormProperty
    )

    suspend fun removeTransaction(activityId: Long, transactionId: Long)
    suspend fun postTransactionParticipants(
        activityId: Long,
        transactionId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    )
}