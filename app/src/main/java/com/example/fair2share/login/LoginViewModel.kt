package com.example.fair2share.login

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.LoginProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AccountApi.sharedPreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class LoginViewModel(var sharedPreferences: SharedPreferences) : ViewModel() {

    var loginData: LoginProperty

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    init {
        AccountApi.sharedPreferences = sharedPreferences
        loginData = LoginProperty.makeEmpty()
    }

    fun login(defaultFailedMessage: String) {
        _coroutineScope.launch {
            try {
                val getJWTDeffered = AccountApi.retrofitService.login(loginData.makeDTO())
                val token = getJWTDeffered.await()
                val edit = sharedPreferences.edit()
                edit.putString("token", token)
                edit.apply()

                _loggedIn.value = true
            } catch (e: HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e, defaultFailedMessage)
            } catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }
}
