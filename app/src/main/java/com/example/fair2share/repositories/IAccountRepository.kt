package com.example.fair2share.repositories

import android.content.res.Resources
import androidx.lifecycle.LiveData
import com.example.fair2share.models.dto_models.LoginDTOProperty
import com.example.fair2share.models.dto_models.RegisterDTOProperty
import com.example.fair2share.models.formdata_models.LoginFormProperty
import com.example.fair2share.models.formdata_models.RegisterFormProperty

interface IAccountRepository {
    val loggedIn: LiveData<Boolean>
    val errorMessage: LiveData<String>

    fun login(resources: Resources, loginData: LoginFormProperty)
    fun register(resources: Resources, registerData: RegisterFormProperty)
}