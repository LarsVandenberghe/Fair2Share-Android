package com.example.fair2share

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.fair2share.network.AccountApi


class LoginActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private var isOnLoginFragment: Boolean = false
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedPreferences()
        setContentView(R.layout.activity_login)

        navController = this.findNavController(R.id.navhostfragment_login)
        NavigationUI.setupActionBarWithNavController(this, navController)
        appBarConfiguration = AppBarConfiguration(navController.graph)
    }

    override fun onBackPressed() {
        if (isOnLoginFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return if (isOnLoginFragment) {
            finish()
            false
        } else {
            NavigationUI.navigateUp(navController, appBarConfiguration)
        }
    }

    fun setIsOnLoginFragment(bool: Boolean) {
        isOnLoginFragment = bool
    }

    private fun setSharedPreferences() {
        AccountApi.sharedPreferences =
            this.getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
    }
}
