package com.example.fair2share

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.fair2share.network.AccountApi


class StartUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedPreferences()
        setContentView(R.layout.activity_startup)
    }

    private fun setSharedPreferences() {
        AccountApi.sharedPreferences =
            this.getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
    }
}
