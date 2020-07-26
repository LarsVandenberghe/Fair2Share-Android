package com.example.fair2share

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.activity_start_up.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import androidx.navigation.fragment.NavHostFragment


class StartUpActivity : AppCompatActivity() {
    var onLoginFragment: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)
        setSupportActionBar(toolbar)
    }

    override fun onBackPressed() {
        if (onLoginFragment){
            finish()
        } else {
            super.onBackPressed()
        }
    }
}
