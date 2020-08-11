package com.example.fair2share.exceptions

import android.content.res.Resources

class InvalidFormDataException(private val exceptionsList: List<Int>) : IllegalArgumentException() {
    fun buildErrorMessage(resources: Resources): String {
        val stringBuilder = StringBuilder()
        exceptionsList.forEach {
            stringBuilder.append(String.format("%s%n", resources.getString(it)))
        }
        return stringBuilder.toString()
    }

}