package com.example.fair2share.activity.transactions

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.*
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityTransactionsFragmentViewModel(var activity : ActivityDTOProperty):ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _transactions = MutableLiveData<List<TransactionDTOProperty>>()
    val transactions: LiveData<List<TransactionDTOProperty>>
        get() = _transactions

    private val _participants = MutableLiveData<List<ProfileDTOProperty>>()

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _navigate = MutableLiveData<Boolean>()
    val navigate: LiveData<Boolean>
        get() = _navigate

    fun update(){
        coroutineScope.launch {
            activity = ActivityApi.retrofitService.getActivity(activity.activityId!!).await()
            _transactions.value = ActivityApi.retrofitService.getActivityTransactions(activity.activityId!!).await()
            _participants.value = ActivityApi.retrofitService.getActivityParticipants(activity.activityId!!).await().participants!!
        }
    }


    fun removeActivity(){
        coroutineScope.launch {
            val a = ActivityApi.retrofitService.removeActivity(activity.activityId!!).await()
            if (!a.isSuccessful){
                _errorMessage.value = a.errorBody()!!.charStream().toString()
            } else {
                _navigate.value = true
            }
        }
    }
}