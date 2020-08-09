package com.example.fair2share.models.formdata_models

import com.example.fair2share.models.dto_models.LoginDTOProperty
import com.example.fair2share.models.dto_models.RegisterDTOProperty

data class LoginFormProperty(
    var email: String,
    var password: String
) {
    companion object {
        fun makeEmpty(): LoginFormProperty {
            return LoginFormProperty("", "")
        }
    }

    fun makeDTO(): LoginDTOProperty {
        return LoginDTOProperty(email, password)
    }
}

data class RegisterFormProperty(
    var email: String,
    var password: String,
    var firstName: String,
    var lastName: String,
    var passwordConfirmation: String
) {

    companion object {
        fun makeEmpty(): RegisterFormProperty {
            return RegisterFormProperty("", "", "", "", "")
        }
    }

    fun makeDTO(): RegisterDTOProperty {
        return RegisterDTOProperty(email, password, firstName, lastName, passwordConfirmation)
    }
}