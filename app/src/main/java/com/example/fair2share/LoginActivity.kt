package com.example.fair2share

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController

import androidx.navigation.NavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI


class LoginActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration : AppBarConfiguration
    private var isOnLoginFragment: Boolean = false
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //setSupportActionBar(toolbar)

        navController = this.findNavController(R.id.loginNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)

    }

    override fun onBackPressed() {
        if (isOnLoginFragment){
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        if (isOnLoginFragment){
            finish()
            return false
        } else {
            return NavigationUI.navigateUp(navController, appBarConfiguration)
        }
    }

    fun setIsOnLoginFragment(bool: Boolean){
        isOnLoginFragment = bool
    }
}
