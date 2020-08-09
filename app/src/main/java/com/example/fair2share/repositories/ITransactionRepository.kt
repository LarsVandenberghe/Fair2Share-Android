package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty

interface ITransactionRepository {
    val errorMessage: LiveData<String>
    val success: LiveData<Boolean>
    val navigate: LiveData<Boolean>
    val transaction: LiveData<TransactionDTOProperty>
    val resetSelected: LiveData<Boolean>

    fun update(resources: Resources, activityId: Long, transactionId: Long)
    fun createOrUpdate(
        resources: Resources,
        isNewTransaction: Boolean,
        activity: ActivityDTOProperty,
        transaction: TransactionFormProperty
    )

    fun removeTransaction(resources: Resources, activityId: Long, transactionId: Long)
    fun postTransactionParticipants(
        resources: Resources,
        activityId: Long,
        transactionId: Long,
        toBeAdded: List<Long>,
        toBeRemoved: List<Long>
    )
}