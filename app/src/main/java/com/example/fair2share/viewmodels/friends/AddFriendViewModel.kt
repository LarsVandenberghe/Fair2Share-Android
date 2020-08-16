package com.example.fair2share.viewmodels.friends

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.exceptions.CustomHttpException
import com.example.fair2share.repositories.IProfileRepository
import com.example.fair2share.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddFriendViewModel(
    private val myProfileEmailAddress: String,
    private val profileRepository: IProfileRepository
) : ViewModel() {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _success = MutableLiveData<Boolean>()
    val success: LiveData<Boolean>
        get() = _success

    fun addFriendByEmail(email: String, resources: Resources) {
        _coroutineScope.launch {
            try {
                profileRepository.addFriendByEmail(myProfileEmailAddress, email)
            } catch (e: HttpException) {
                _errorMessage.postValue(Utils.formExceptionsToString(e))
            } catch (e: CustomHttpException){
                _errorMessage.postValue(resources.getString(e.stringId))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}