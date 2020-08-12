package com.example.fair2share.models.dto_models


import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.utils.Constants
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
        val emailPattern = Pattern.compile(Constants.REGEX_EMAIL_ADDRESS_PATTERN)
        if (!emailPattern.matcher(email).matches()) {
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
        val spacesAndAllUnicodeChars = Pattern.compile(Constants.REGEX_SPACES_AND_UNICODE)
        val password8CharsACapANumber =
            Pattern.compile(Constants.REGEX_PASSWORD)
        val emailPattern = Pattern.compile(Constants.REGEX_EMAIL_ADDRESS_PATTERN)

        if (!spacesAndAllUnicodeChars.matcher(firstName).matches()) {
            exceptionsList.add(R.string.firtsname_not_valid)
        }

        if (!spacesAndAllUnicodeChars.matcher(lastName).matches()) {
            exceptionsList.add(R.string.lastname_not_valid)
        }

        if (!emailPattern.matcher(email).matches()) {
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