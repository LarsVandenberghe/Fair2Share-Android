package com.example.fair2share.viewmodels.login

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.fair2share.models.formdata_models.RegisterFormProperty
import com.example.fair2share.repositories.AccountRepository
import com.example.fair2share.repositories.IAccountRepository

class RegisterViewModel : ViewModel() {
    private var repository: IAccountRepository = AccountRepository()
    var registerData: RegisterFormProperty = RegisterFormProperty.makeEmpty()

    val loggedIn: LiveData<Boolean> = repository.loggedIn
    val errorMessage: LiveData<String> = repository.errorMessage

    fun register(resources: Resources) {
        repository.register(resources, registerData.makeDTO())
    }
}