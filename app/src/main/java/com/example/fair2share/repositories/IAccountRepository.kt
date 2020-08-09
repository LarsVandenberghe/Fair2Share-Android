package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.LoginDTOProperty
import com.example.fair2share.models.dto_models.RegisterDTOProperty

interface IAccountRepository {
    val loggedIn: LiveData<Boolean>
    val errorMessage: LiveData<String>

    fun login(resources: Resources, loginData: LoginDTOProperty)
    fun register(resources: Resources, registerData: RegisterDTOProperty)
}