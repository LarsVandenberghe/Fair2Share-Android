package com.example.fair2share

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.databinding.ActivityMainBinding
import com.example.fair2share.databinding.NavHeaderBinding
import com.example.fair2share.profile.FragmentProfileViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        // prevent nav gesture if not on start destination
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }

        NavigationUI.setupWithNavController(binding.navView, navController)
        (drawerLayout.navView as NavigationView).setNavigationItemSelectedListener {
            if (  it.itemId == R.id.logoutActivity ){
                startActivity(Intent(this, LoginActivity::class.java))
            }

            if ( it.itemId == R.id.friendsActivity ){
                startActivity(Intent(this, FriendsActivity::class.java))
            }
            return@setNavigationItemSelectedListener false
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    fun bindProfileToNavHeader(vm: FragmentProfileViewModel){
        var bind: NavHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_header, binding.navView, true)
        vm.profile.observe(this, Observer { data ->
            bind.profile = data

            Glide.with(bind.navProfileImg.context)
                .load(vm.getProfilePicUrl(data))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                ).into(bind.navProfileImg)
        })
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.friendsActivity)?.title = String.format("(%d)", 1)
        return super.onPrepareOptionsMenu(menu)
    }
}
