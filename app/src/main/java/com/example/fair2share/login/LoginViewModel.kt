package com.example.fair2share.login

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.models.formdata_models.LoginFormProperty
import com.example.fair2share.repositories.AccountRepository
import com.example.fair2share.repositories.IAccountRepository

class LoginViewModel : ViewModel() {
    private val repository: IAccountRepository = AccountRepository()
    var loginData: LoginFormProperty = LoginFormProperty.makeEmpty()

    val loggedIn: LiveData<Boolean> = repository.loggedIn
    val errorMessage: LiveData<String> = repository.errorMessage

    fun login(resources: Resources) {
        repository.login(resources, loginData.makeDTO())
    }
}
