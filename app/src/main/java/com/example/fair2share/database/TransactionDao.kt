package com.example.fair2share.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fair2share.models.database_models.TransactionDatabaseProperty

@Dao
interface TransactionDao {
    @Query("select * from transaction_table where profileId = :profileId and activityId = :activityId and transactionId = :transactionId")
    fun getTransaction(profileId:Long, activityId:Long, transactionId:Long): LiveData<TransactionDatabaseProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(vararg transaction: TransactionDatabaseProperty)
}