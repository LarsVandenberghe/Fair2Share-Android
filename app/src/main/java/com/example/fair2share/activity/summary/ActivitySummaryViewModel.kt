package com.example.fair2share.activity.summary

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.ActivityApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ActivitySummaryViewModel(val activity : ActivityDTOProperty) : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _summary = MutableLiveData<List<Pair<ProfileDTOProperty, Double>>>()
    val summary: LiveData<List<Pair<ProfileDTOProperty, Double>>>
        get() = _summary

    private val _participants = MutableLiveData<List<ProfileDTOProperty>>()


    fun update(){
        coroutineScope.launch {
            _participants.value = ActivityApi.retrofitService.getActivityParticipants(activity.activityId!!).await().participants
            _summary.value = ActivityApi.retrofitService.getActivitySummary(activity.activityId).await().map {
                val profile = (_participants.value as List).find {participant ->
                    participant.profileId == it.key.toLong()
                }
                val out : Pair<ProfileDTOProperty, Double> = Pair(
                    first = profile!!, second = it.value
                )
                out
            }
        }
    }
}
