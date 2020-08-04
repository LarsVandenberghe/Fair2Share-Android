package com.example.fair2share.friends

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.network.FriendRequestApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AddFriendViewModel(val myProfileEmailAddress: String) : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _succes = MutableLiveData<Boolean>()
    val succes: LiveData<Boolean>
        get() = _succes

    fun addFriendByEmail(email: String, errorSendYourSelf:String, alreadyAPending:String, emailNotFound:String){
        _coroutineScope.launch {
            if (myProfileEmailAddress.equals(email)){
                _errorMessage.value = errorSendYourSelf
            } else {
                val getJWTDeffered = FriendRequestApi.retrofitService.addFriendByEmail(email)
                val result = getJWTDeffered.await()
                when (result.code()){
                    500 -> _errorMessage.value = alreadyAPending
                    404 -> _errorMessage.value = emailNotFound
                    else -> {
                        try {
                            Utils.throwExceptionIfHttpNotSuccessful(result)
                            _succes.value = true
                        } catch (e:HttpException){
                            _errorMessage.value = Utils.formExceptionsToString(e)
                        } catch (t: Throwable){
                            _errorMessage.value = t.message
                        }
                    }
                }
            }
        }
    }
}