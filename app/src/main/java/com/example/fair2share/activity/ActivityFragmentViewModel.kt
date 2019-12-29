package com.example.fair2share.activity

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.KeyValueProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivityFragmentViewModel(var activity : ActivityProperty):ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val _transactions = MutableLiveData<List<TransactionProperty>>()
    val transactions: LiveData<List<TransactionProperty>>
        get() = _transactions

    private val _participants = MutableLiveData<List<ProfileProperty>>()

    private val _summary = MutableLiveData<List<Pair<ProfileProperty, Double>>>()
    val summary: LiveData<List<Pair<ProfileProperty, Double>>>
        get() = _summary

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
            _participants.value = ActivityApi.retrofitService.getActivityParticipants(activity.activityId!!).await().participants
            _summary.value = ActivityApi.retrofitService.getActivitySummary(activity.activityId!!).await().map {
                var profile = (_participants.value as List).find {participant ->
                    participant.profileId == it.key.toLong()
                }
                var out : Pair<ProfileProperty, Double> = Pair(
                    first = profile!!, second = it.value
                )
                out
            }
        }
    }


    fun removeActivity(activity: ActivityProperty){
        coroutineScope.launch {
            val a = ActivityApi.retrofitService.removeActivity(activity.activityId!!).await()
            if (!a.isSuccessful){
                _errorMessage.value = a.errorBody()!!.string()
            } else {
                _navigate.value = true
            }
        }
    }
}