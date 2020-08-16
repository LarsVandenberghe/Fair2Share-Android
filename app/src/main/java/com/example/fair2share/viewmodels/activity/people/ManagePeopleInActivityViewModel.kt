package com.example.fair2share.viewmodels.activity.people

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.exceptions.CustomHttpException
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.repositories.IActivityRepository
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class ManagePeopleInActivityViewModel(
    private var activityArg: ActivityDTOProperty,
    private val activityRepository: IActivityRepository,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val activity: LiveData<ActivityDTOProperty> = activityRepository.activity
    val profile: LiveData<ProfileDTOProperty> = profileRepository.profile

    private val _initialParticipants: LiveData<List<ProfileDTOProperty>> =
        Transformations.map(activity) {
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

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    init {
        candidatesAndParticipantsListenToUpdates()
        activity.observeForever {
            activityArg = it
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

        _coroutineScope.launch {
            try {
                activityRepository.postActivityParticipants(
                    activityArg.activityId!!,
                    toBeAdded,
                    toBeRemoved
                )
                _success.postValue(true)
            } catch (e: HttpException) {
                _errorMessage.postValue(Utils.formExceptionsToString(e))
                resetSelected()
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }

        update(resources)
    }


    fun resetSelected() {
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
    }

    fun update(resources: Resources) {
        _coroutineScope.launch {
            try {
                activityRepository.update(activityArg.activityId!!)
                profileRepository.update()
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (e: CustomHttpException){
                _errorMessage.postValue(resources.getString(e.stringId))
            } catch (t: Throwable) {
                if (AuthInterceptor.throwableIs401(t)) {
                    _errorMessage.postValue(resources.getString(R.string.fragment_startup_tokenexipred))
                } else {
                    _errorMessage.postValue(t.message)
                }
            }
        }
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
        } ?: ArrayList()

        friends?.let {
            participants.addAll(it.filter { participant ->
                !candidateIds.contains(participant.profileId)
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