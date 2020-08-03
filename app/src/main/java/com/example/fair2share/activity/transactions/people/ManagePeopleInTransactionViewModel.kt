package com.example.fair2share.activity.transactions.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty
import com.example.fair2share.network.ActivityApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class ManagePeopleInTransactionViewModel(private val activity: ActivityDTOProperty, private val transaction: TransactionDTOProperty) : ViewModel() {
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _friends = MutableLiveData<List<ProfileDTOProperty>>()
    private val _initalParticipants = MutableLiveData<List<ProfileDTOProperty>>()
    private val _toBeAdded = MutableLiveData<List<Long>>()
    private val _toBeRemoved = MutableLiveData<List<Long>>()

    private val _participants = MutableLiveData<List<ProfileDTOProperty>>()
    val participants: LiveData<List<ProfileDTOProperty>>
        get() = _participants

    private val _candidates = MutableLiveData<List<ProfileDTOProperty>>()
    val candidates: LiveData<List<ProfileDTOProperty>>
        get() = _candidates

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

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
        coroutineScope.launch {
            try {
                val toBeAdded = _toBeAdded.value!!
                val toBeRemoved = _toBeRemoved.value!!
                if (toBeRemoved.size > 0) {
                    val result = ActivityApi.retrofitService.removeTransactionParticipants(activity.activityId!!, transaction.transactionId!!, toBeRemoved).await()
                    Utils.throwExceptionIfHttpNotSuccessful(result)
                }
                if (toBeAdded.size > 0) {
                    val result = ActivityApi.retrofitService.addTransactionParticipants(activity.activityId!!, transaction.transactionId!!, toBeAdded).await()
                    Utils.throwExceptionIfHttpNotSuccessful(result)
                }
                _success.value = true
            } catch (e: HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e)
                resetSelected()
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }


    fun resetSelected(){
        _toBeAdded.value = ArrayList()
        _toBeRemoved.value = ArrayList()
    }

    private fun update(){
        coroutineScope.launch {
            _initalParticipants.value = ActivityApi
                .retrofitService
                .getActivityTransactionById(activity.activityId!!, transaction.transactionId!!)
                .await().profilesInTransaction

            _friends.value = ActivityApi
                .retrofitService
                .getActivityParticipants(activity.activityId)
                .await().participants
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

    private fun findParticipantsBasedOnCandidates(): List<ProfileDTOProperty> {
        val participants = ArrayList<ProfileDTOProperty>()
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

    private fun findCandidates() : List<ProfileDTOProperty> {
        val friends =_friends.value
        var initialParticips = _initalParticipants.value

        initialParticips = initialParticips?.filter {
            (friends?.map {friend ->
                friend.profileId
            } ?: ArrayList()).contains(it.profileId)
        } ?: ArrayList()

        val toBeRemoved = _toBeRemoved.value!!
        val toBeAdded = _toBeAdded.value!!
        val candidates = ArrayList<ProfileDTOProperty>()

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