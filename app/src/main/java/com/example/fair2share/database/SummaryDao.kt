package com.example.fair2share.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fair2share.models.database_models.ActivitySummaryDatabaseProperty

@Dao
interface SummaryDao {
    @Query("select * from activity_summary_table where profileId = :profileId and activityId = :activityId")
    fun getActivitySummary(
        profileId: Long,
        activityId: Long
    ): LiveData<ActivitySummaryDatabaseProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivitySummary(vararg activity: ActivitySummaryDatabaseProperty)
}