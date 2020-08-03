package com.example.fair2share.models.dto_models

data class LoginDTOProperty(
    val email: String,
    val password: String
)

data class RegisterDTOProperty(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passwordConfirmation: String
)