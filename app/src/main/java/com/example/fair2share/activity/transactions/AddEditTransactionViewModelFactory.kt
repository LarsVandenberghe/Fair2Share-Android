package com.example.fair2share.activity.transactions

import android.os.Parcel
import android.os.Parcelable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.fair2share.activity.AddEditActivityFragmentViewModel
import com.example.fair2share.activity.people.ManagePeopleInActivityViewModel
import com.example.fair2share.activity.summary.ActivitySummaryViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import java.lang.IllegalArgumentException

class AddEditTransactionViewModelFactory(private val activity: ActivityDTOProperty, private val transaction: TransactionProperty, val isNewTransaction: Boolean, val database: Fair2ShareDatabase) : ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(AddEditTransactionViewModel::class.java) -> {
                AddEditTransactionViewModel(activity, transaction, isNewTransaction, database) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}