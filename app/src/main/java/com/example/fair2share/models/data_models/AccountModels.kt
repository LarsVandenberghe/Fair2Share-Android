package com.example.fair2share.models.data_models

import com.example.fair2share.models.dto_models.LoginDTOProperty
import com.example.fair2share.models.dto_models.RegisterDTOProperty

data class LoginProperty(
    var email: String,
    var password: String
){
    companion object {
        fun makeEmpty(): LoginProperty {
            return LoginProperty("", "")
        }
    }

    fun makeDTO(): LoginDTOProperty {
        return LoginDTOProperty(email, password)
    }
}

data class RegisterProperty(
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String,
    var passwordConfirmation: String
){

    companion object {
        fun makeEmpty(): RegisterProperty {
            return RegisterProperty("", "", "", "", "")
        }
    }

    fun makeDTO(): RegisterDTOProperty {
        return RegisterDTOProperty(email, password, firstName, lastName, passwordConfirmation)
    }
}