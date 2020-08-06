package com.example.fair2share.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.fair2share.models.database_models.ProfileDatabaseProperty

@Dao
interface ProfileDao {
    @Query("select * from profile_table where profileId = :profileId")
    fun getProfile(profileId: Long): LiveData<ProfileDatabaseProperty>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProfile(vararg profile: ProfileDatabaseProperty)
}