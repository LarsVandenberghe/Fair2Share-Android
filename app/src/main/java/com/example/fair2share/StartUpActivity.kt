package com.example.fair2share

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import kotlinx.android.synthetic.main.activity_start_up.*

class StartUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)
        setSupportActionBar(toolbar)

        val handler = Handler()
        handler.postDelayed({
            this.handleLoginState()
        }, 1000)
    }

    private fun handleLoginState(){
        val navController = findNavController(R.id.startUpNavHostFragment)

        navController.navigate(R.id.action_startUpFragment_to_loginFragment)
    }
}
