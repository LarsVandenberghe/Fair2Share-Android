package com.example.fair2share.login

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.Utils
import com.example.fair2share.data_models.RegisterProperty
import com.example.fair2share.network.AccountApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class RegisterViewModel(var sharedPreferences: SharedPreferences): ViewModel() {
    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage

    init {
        AccountApi.sharedPreferences = sharedPreferences
    }

    fun register(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        passwordConfirmation: String
    ){
        _coroutineScope.launch {
            try {
                val getJWTDeffered = AccountApi.retrofitService.register(
                    RegisterProperty(
                        email,
                        password,
                        firstName,
                        lastName,
                        passwordConfirmation
                    )
                )
                val token = getJWTDeffered.await()
                val edit = sharedPreferences.edit()
                edit.putString("token", token)
                edit.apply()
                _loggedIn.value = true
            } catch (e: HttpException){
                _errorMessage.value = Utils.formExceptionsToString(e)
            }catch (t: Throwable){
                _errorMessage.value = t.message
            }
        }
    }
}