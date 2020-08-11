package com.example.fair2share.models.dto_models


import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import java.util.regex.Pattern

data class LoginDTOProperty(
    val email: String,
    val password: String
) {
    init {
        checkValid()
    }

    private fun checkValid() {
        val exceptionsList = ArrayList<Int>()
        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            exceptionsList.add(R.string.email_not_valid)
            throw InvalidFormDataException(exceptionsList)
        }
    }
}

data class RegisterDTOProperty(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passwordConfirmation: String
) {
    init {
        checkValid()
    }

    private fun checkValid() {
        val exceptionsList = ArrayList<Int>()
        val spacesAndAllUnicodeChars = Pattern.compile("^[0-9\\p{L} .'-]+$")
        val password8CharsACapANumber =
            Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$")

        if (!spacesAndAllUnicodeChars.matcher(firstName).matches()) {
            exceptionsList.add(R.string.firtsname_not_valid)
        }

        if (!spacesAndAllUnicodeChars.matcher(lastName).matches()) {
            exceptionsList.add(R.string.lastname_not_valid)
        }

        if (!EMAIL_ADDRESS.matcher(email).matches()) {
            exceptionsList.add(R.string.email_not_valid)
        }

        if (password != passwordConfirmation) {
            exceptionsList.add(R.string.passwords_not_equal)
        }

        if (!password8CharsACapANumber.matcher(password).matches()) {
            exceptionsList.add(R.string.password_not_valid)
        }

        if (exceptionsList.size > 0) {
            throw InvalidFormDataException(exceptionsList)
        }
    }
}

// This regex is built into android.util.Patterns, but returns null on parameterized tests.
private val EMAIL_ADDRESS = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)