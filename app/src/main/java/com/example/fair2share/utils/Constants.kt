package com.example.fair2share.utils

class Constants {
    companion object{
        const val SHARED_PREFERENCES_KEY_TOKEN = "token"
        const val SHARED_PREFERENCES_KEY_PROFILEID = "profileId"
        const val SHARED_PREFERENCES_PROFILEID_NOT_SET = "ProfileID sharedPreferences not set!"
        const val REFRESH_ANIMATION_TIMER: Long = 700
        const val REFRESH_ANIMATION_TIMER_KEY = "StopRefreshingAnimationAfter700ms"
        const val UNKNOWN_VIEWMODEL = "Unknown ViewModel class"
        const val DATABASE_NAME = "databasefair2share"

        // This regex is built into android.util.Patterns, but returns null on parameterized tests.
        const val REGEX_EMAIL_ADDRESS_PATTERN = "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+"
        const val REGEX_SPACES_AND_UNICODE = "^[0-9\\p{L} .'-]+$"
        const val REGEX_PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,}$"
    }
}