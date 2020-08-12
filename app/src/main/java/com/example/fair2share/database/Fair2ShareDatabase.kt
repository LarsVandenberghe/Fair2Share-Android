package com.example.fair2share.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.ActivitySummaryDatabaseProperty
import com.example.fair2share.models.database_models.ProfileDatabaseProperty
import com.example.fair2share.models.database_models.TransactionDatabaseProperty
import com.example.fair2share.utils.Constants

@Database(
    entities = [
        ProfileDatabaseProperty::class,
        ActivityDatabaseProperty::class,
        ActivitySummaryDatabaseProperty::class,
        TransactionDatabaseProperty::class
    ],
    version = 1,
    exportSchema = false
)
abstract class Fair2ShareDatabase : RoomDatabase() {
    abstract val profileDao: ProfileDao
    abstract val activityDao: ActivityDao
    abstract val summaryDao: SummaryDao
    abstract val transactionDao: TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: Fair2ShareDatabase? = null

        fun getInstance(context: Context): Fair2ShareDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        Fair2ShareDatabase::class.java,
                        Constants.DATABASE_NAME
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}