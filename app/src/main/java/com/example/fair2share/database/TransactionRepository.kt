package com.example.fair2share.database

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.network.ActivityApi
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository(private val database: Fair2ShareDatabase) {
    private val jsonAdapter = Moshi.Builder().build().adapter(TransactionDTOProperty::class.java)



    fun getTransaction(profileId: Long, activityId: Long, transactionId: Long): LiveData<TransactionDTOProperty> {
        val data = database.transactionDao.getTransaction(profileId, activityId, transactionId)
        return Transformations.map(data){
            jsonAdapter.fromJson(it.data)
        }
    }

    suspend fun refreshTransaction(profileId: Long, activityId: Long, transactionId: Long) {
        withContext(Dispatchers.IO){
            val data = ActivityApi.retrofitService.getActivityTransactionById(activityId, transactionId).await().makeDatabaseModel(profileId, activityId)
            database.transactionDao.insertTransaction(data)
        }
    }
}