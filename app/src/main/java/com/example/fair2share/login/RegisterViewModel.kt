package com.example.fair2share.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.RegisterProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException

class RegisterViewModel : ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)
    var registerData: RegisterProperty = RegisterProperty.makeEmpty()

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    fun register() {
        _coroutineScope.launch {
            try {
                val getJWTDeffered = AccountApi.retrofitService.register(registerData.makeDTO())
                val token = getJWTDeffered.await()
                val edit = sharedPreferences.edit()
                edit.putString("token", token)
                edit.apply()
                _loggedIn.value = true
            } catch (e: HttpException) {
                _errorMessage.value = Utils.formExceptionsToString(e)
            } catch (t: Throwable) {
                _errorMessage.value = t.message
            }
        }
    }
}