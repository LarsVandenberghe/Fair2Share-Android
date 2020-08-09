package com.example.fair2share.activity.people

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.repositories.ActivityRepository
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.repositories.ProfileRepository

class ManagePeopleInActivityViewModel(
    private var activityArg: ActivityDTOProperty,
    database: Fair2ShareDatabase
) : ViewModel() {
    private val activityRepository: IActivityRepository =
        ActivityRepository(database)
    private val profileRepository: IProfileRepository = ProfileRepository(database)

    val success: LiveData<Boolean> = activityRepository.success
    val errorMessage: LiveData<String> = activityRepository.errorMessage
    val acivity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile

    private val _initialParticipants: LiveData<List<ProfileDTOProperty>> =
        Transformations.map(acivity) {
            it.participants
        }
    private val _friends: LiveData<List<ProfileDTOProperty>> = Transformations.map(profile) {
        it.friends
    }
    private val _toBeAdded = MutableLiveData<List<Long>>()
    private val _toBeRemoved = MutableLiveData<List<Long>>()

    private val _participants = MutableLiveData<List<ProfileDTOProperty>>()
    val participants: LiveData<List<ProfileDTOProperty>>
        get() = _participants

    private val _candidates = MutableLiveData<List<ProfileDTOProperty>>()
    val candidates: LiveData<List<ProfileDTOProperty>>
        get() = _candidates


    init {
        candidatesAndParticipantsListenToUpdates()
        acivity.observeForever {
            activityArg = it
        }

        activityRepository.resetSelected.observeForever {
            if (it) {
                resetSelected()
            }
        }
    }

    fun addToParticipants(id: Long) {
        val toBeAdded = _toBeAdded.value!!.toMutableList()
        val toBeRemoved = _toBeRemoved.value!!.toMutableList()
        if (toBeRemoved.contains(id)) {
            toBeRemoved.remove(id)
            _toBeRemoved.value = toBeRemoved
        } else {
            toBeAdded.add(id)
            _toBeAdded.value = toBeAdded
        }
    }

    fun removeFromParticipants(id: Long) {
        val toBeAdded = _toBeAdded.value!!.toMutableList()
        val toBeRemoved = _toBeRemoved.value!!.toMutableList()
        if (toBeAdded.contains(id)) {
            toBeAdded.remove(id)
            _toBeAdded.value = toBeAdded
        } else {
            toBeRemoved.add(id)
            _toBeRemoved.value = toBeRemoved
        }
    }

    fun confirm(resources: Resources) {
        val toBeAdded = _toBeAdded.value!!
        val toBeRemoved = _toBeRemoved.value!!
        activityRepository.postActivityParticipants(
            resources,
            activityArg.activityId!!,
            toBeAdded,
            toBeRemoved
        )
        update(resources)
    }


    fun resetSelected() {
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
    }

    fun update(resources: Resources) {
        activityRepository.update(resources, activityArg.activityId!!)
        profileRepository.update(resources)
    }

    private fun candidatesAndParticipantsListenToUpdates() {
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
        _friends.observeForever {
            updateCandidatesAndParticipants()
        }

        _initialParticipants.observeForever {
            updateCandidatesAndParticipants()
        }

        _toBeAdded.observeForever {
            updateCandidatesAndParticipants()
        }

        _toBeRemoved.observeForever {
            updateCandidatesAndParticipants()
        }
    }

    private fun updateCandidatesAndParticipants() {
        _candidates.value = findCandidates()
        _participants.value = findParticipantsBasedOnCandidates()
    }

    private fun findParticipantsBasedOnCandidates(): List<ProfileDTOProperty> {
        val participants = ArrayList<ProfileDTOProperty>()
        val candidates = _candidates.value
        val friends = _friends.value
        val candidateIds = candidates?.map {
            it.profileId
        } ?: ArrayList<Long>()

        friends?.let {
            participants.addAll(it.filter { paticipant ->
                !candidateIds.contains(paticipant.profileId)
            })
        }
        return participants
    }

    private fun findCandidates(): List<ProfileDTOProperty> {
        val friends = _friends.value
        var initialParticips = _initialParticipants.value

        initialParticips = initialParticips?.filter {
            (friends?.map { friend ->
                friend.profileId
            } ?: ArrayList()).contains(it.profileId)
        } ?: ArrayList()

        val toBeRemoved = _toBeRemoved.value!!
        val toBeAdded = _toBeAdded.value!!
        val candidates = ArrayList<ProfileDTOProperty>()

        if (friends != null) {
            candidates.addAll(friends.filter {
                val initialParticipsIds = initialParticips.map { part -> part.profileId }
                if (initialParticipsIds.contains(it.profileId)) {
                    false
                } else {
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