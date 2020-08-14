package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.dto_models.LoginDTOProperty
import com.example.fair2share.models.dto_models.RegisterDTOProperty
import com.example.fair2share.models.formdata_models.LoginFormProperty
import com.example.fair2share.models.formdata_models.RegisterFormProperty
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.utils.Constants
import com.example.fair2share.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class AccountRepository : IAccountRepository {
    private var _repositoryJob = Job()
    private val _coroutineScope = CoroutineScope(_repositoryJob + Dispatchers.IO)

    private val _loggedIn = MutableLiveData<Boolean>()
    override val loggedIn: LiveData<Boolean>
        get() = _loggedIn

    private val _errorMessage = MutableLiveData<String>()
    override val errorMessage: LiveData<String>
        get() = _errorMessage


    override fun login(resources: Resources, loginData: LoginFormProperty) {
        _coroutineScope.launch {
            try {
                val getJWTDeferred = AccountApi.retrofitService.login(loginData.makeDTO())
                val token = getJWTDeferred.await()
                val edit = AccountApi.sharedPreferences.edit()
                edit.putString(Constants.SHARED_PREFERENCES_KEY_TOKEN, token)
                edit.apply()
                AuthInterceptor.loginSucceeded()

                _loggedIn.postValue(true)
            } catch (e: HttpException) {
                _errorMessage.postValue(
                    Utils.formExceptionsToString(
                        e,
                        resources.getString(R.string.fragment_login_fail)
                    )
                )
            } catch (e: ConnectException) {
                AccountApi.setIsOfflineValue(true)
                _errorMessage.postValue(resources.getString(R.string.offline_error))
            } catch (e: InvalidFormDataException) {
                _errorMessage.postValue(e.buildErrorMessage(resources))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }

    override fun register(resources: Resources, registerData: RegisterFormProperty) {
        _coroutineScope.launch {
            try {
                val getJWTDeferred = AccountApi.retrofitService.register(registerData.makeDTO())
                val token = getJWTDeferred.await()
                val edit = AccountApi.sharedPreferences.edit()
                edit.putString(Constants.SHARED_PREFERENCES_KEY_TOKEN, token)
                edit.apply()
                _loggedIn.postValue(true)
            } catch (e: HttpException) {
                _errorMessage.postValue(Utils.formExceptionsToString(e))
            } catch (e: InvalidFormDataException) {
                _errorMessage.postValue(e.buildErrorMessage(resources))
            } catch (t: Throwable) {
                _errorMessage.postValue(t.message)
            }
        }
    }
}