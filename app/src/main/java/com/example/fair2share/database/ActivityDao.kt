package com.example.fair2share.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fair2share.models.database_models.ActivityDatabaseProperty

@Dao
interface ActivityDao {
    @Query("select * from activity_table where profileId = :profileId and activityId = :activityId")
    fun getActivity(profileId: Long, activityId: Long): LiveData<ActivityDatabaseProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivity(vararg activity: ActivityDatabaseProperty)
}