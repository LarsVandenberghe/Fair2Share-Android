package com.example.fair2share.friends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.data_models.FriendRequestStates
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.FriendRequestApi
import com.example.fair2share.network.ProfileApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.logging.Logger

class AddFriendViewModel(val myProfileEmailAddress: String) : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private val _succes = MutableLiveData<Boolean>()
    val succes: LiveData<Boolean>
        get() = _succes




    //TODO: Implement stringify
    fun addFriendByEmail(email: String){
        _coroutineScope.launch {
            if (myProfileEmailAddress.equals(email)){
                _errorMessage.value = "You can't send yourself a friend request."
            } else {
                val getJWTDeffered = FriendRequestApi.retrofitService.addFriendByEmail(email)
                val result = getJWTDeffered.await()
                when (result.code()){
                    500 -> _errorMessage.value = "Something went wrong. Maybe you have a pending friend request from this email?"
                    404 -> _errorMessage.value = "Email does not exist."
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