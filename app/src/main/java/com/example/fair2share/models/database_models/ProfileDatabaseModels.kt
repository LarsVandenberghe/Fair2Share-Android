package com.example.fair2share.models.database_models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class ProfileDatabaseProperty(
    @PrimaryKey
    val profileId: Long,
    val data: String
)