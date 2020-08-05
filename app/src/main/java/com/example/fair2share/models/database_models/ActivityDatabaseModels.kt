package com.example.fair2share.models.database_models

import androidx.room.Entity

@Entity(tableName = "activity_table", primaryKeys = ["profileId", "activityId"])
data class ActivityDatabaseProperty (
    val profileId: Long,
    val activityId: Long,
    val data: String
)

@Entity(tableName = "transaction_table", primaryKeys = ["profileId", "activityId", "transactionId"])
data class TransactionDatabaseProperty(
    val profileId: Long,
    val activityId: Long,
    val transactionId: Long,
    val data: String
)