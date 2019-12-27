package com.example.fair2share.activity

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityFragmentViewModel(val activity : ActivityProperty):ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _transactions = MutableLiveData<List<TransactionProperty>>()
    val transactions: LiveData<List<TransactionProperty>>
        get() = _transactions

    init {
        coroutineScope.launch {
              _transactions.value = ActivityApi.retrofitService.getActivityTransactions(activity.activityId).await()
        }
    }
}