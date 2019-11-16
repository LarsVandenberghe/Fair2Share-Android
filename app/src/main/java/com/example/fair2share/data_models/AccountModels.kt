package com.example.fair2share.data_models

data class LoginProperty(
    val email: String,
    val password: String
)

data class RegisterProperty(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val passwordConfirmation: String
)