package com.example.fair2share.activity.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ManagePeopleInActivityViewModel(private val activity: ActivityProperty) : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _friends = MutableLiveData<List<ProfileProperty>>()
    private val _initalParticipants = MutableLiveData<List<ProfileProperty>>()
    private val _toBeAdded = MutableLiveData<List<Long>>()
    private val _toBeRemoved = MutableLiveData<List<Long>>()

    private val _participants = MutableLiveData<List<ProfileProperty>>()
    val participants: LiveData<List<ProfileProperty>>
        get() = _participants

    private val _candidates = MutableLiveData<List<ProfileProperty>>()
    val candidates: LiveData<List<ProfileProperty>>
        get() = _candidates


    init {
        candidatesAndParticipantsListenToUpdates()
        update()
    }

    fun addToParticipants(id:Long){
        val toBeAdded = _toBeAdded.value!!.toMutableList()
        val toBeRemoved = _toBeRemoved.value!!.toMutableList()
        if (toBeRemoved.contains(id)){
            toBeRemoved.remove(id)
            _toBeRemoved.value = toBeRemoved
        } else {
            toBeAdded.add(id)
            _toBeAdded.value = toBeAdded
        }
    }

    fun removeFromParticipants(id:Long){
        val toBeAdded = _toBeAdded.value!!.toMutableList()
        val toBeRemoved = _toBeRemoved.value!!.toMutableList()
        if (toBeAdded.contains(id)){
            toBeAdded.remove(id)
            _toBeAdded.value = toBeAdded
        } else {
            toBeRemoved.add(id)
            _toBeRemoved.value = toBeRemoved
        }
    }

    fun confirm(){

    }


    fun resetSelected(){
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
    }

    private fun update(){
        coroutineScope.launch {
            _initalParticipants.value = ActivityApi
                .retrofitService
                .getActivityParticipants(activity.activityId!!)
                .await().participants

            _friends.value = ProfileApi.retrofitService.profile().await().friends
        }
    }

    private fun candidatesAndParticipantsListenToUpdates(){
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
        _friends.observeForever {
            updateCandidatesAndParticipants()
        }

        _initalParticipants.observeForever {
            updateCandidatesAndParticipants()
        }

        _toBeAdded.observeForever {
            updateCandidatesAndParticipants()
        }

        _toBeRemoved.observeForever {
            updateCandidatesAndParticipants()
        }
    }

    private fun updateCandidatesAndParticipants(){
        _candidates.value = findCandidates()
        _participants.value = findParticipantsBasedOnCandidates()
    }

    private fun findParticipantsBasedOnCandidates(): List<ProfileProperty> {
        val participants = ArrayList<ProfileProperty>()
        val candidates = _candidates.value
        val friends =_friends.value
        val candidateIds = candidates?.map {
            it.profileId
        } ?: ArrayList<Long>()

        friends?.let{
            participants.addAll(it.filter { paticipant ->
                !candidateIds.contains(paticipant.profileId)
            })
        }
        return participants
    }

    private fun findCandidates() : List<ProfileProperty> {
        val friends =_friends.value
        var initialParticips = _initalParticipants.value

        initialParticips = initialParticips?.filter {
            (friends?.map {friend ->
                friend.profileId
            } ?: ArrayList()).contains(it.profileId)
        } ?: ArrayList()

        val toBeRemoved = _toBeRemoved.value!!
        val toBeAdded = _toBeAdded.value!!
        val candidates = ArrayList<ProfileProperty>()

        if (friends != null){
            candidates.addAll(friends.filter {
                val initialParticipsIds = initialParticips.map{part -> part.profileId }
                if (initialParticipsIds.contains(it.profileId)) {
                    false
                } else{
                    !toBeAdded.contains(it.profileId)
                }
            })
        }

        candidates.addAll(initialParticips.filter {
            toBeRemoved.contains(it.profileId)
        })

        return candidates
    }

}