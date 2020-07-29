package com.example.fair2share

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
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
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.databinding.ActivityMainBinding
import com.example.fair2share.databinding.NavHeaderBinding
import com.example.fair2share.login.AuthInterceptor
import com.example.fair2share.network.AccountApi.sharedPreferences
import com.example.fair2share.profile.ProfileFragmentViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.mainNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)

        preventGestureIfNotOnStartDestination(navController)

        NavigationUI.setupWithNavController(binding.navView, navController)
        setupNavigationListener(navController)

        setupOnTokenExpiredRestartApp()
    }



    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.mainNavHostFragment)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.btnMenuFriends)?.title = String.format("(%d)", 1)
        return super.onPrepareOptionsMenu(menu)
    }

    fun bindProfileToNavHeader(vm: ProfileFragmentViewModel){
        navHeaderBinding = DataBindingUtil.inflate(layoutInflater, R.layout.nav_header, binding.navView, true)
        vm.profile.observe(this, Observer { data ->
            navHeaderBinding.profile = data

            Glide.with(navHeaderBinding.imgNavProfile.context)
                .load(vm.getProfilePicUrl(data))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                ).into(navHeaderBinding.imgNavProfile)
        })
    }

    private fun preventGestureIfNotOnStartDestination(navController: NavController){
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, bundle: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    private fun setupNavigationListener(navController: NavController){
        (drawerLayout.navView as NavigationView).setNavigationItemSelectedListener {
            if (  it.itemId == R.id.btnMenuLogout ){
                sharedPreferences?.edit()?.remove("token")?.apply()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            if ( it.itemId == R.id.btnMenuFriends ){
                val friends = Bundle()
                friends.putParcelableArrayList("friends", navHeaderBinding.profile?.friends as ArrayList<ProfileProperty>)
                navController.navigate(R.id.action_fragmentProfile_to_friendListFragment, friends)
            }
            return@setNavigationItemSelectedListener false
        }
    }

    private fun setupOnTokenExpiredRestartApp() {
        AuthInterceptor.shouldRestart.observe(this, Observer {
            if(it){
                val intent = Intent(this.baseContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
                this.finish()
                Runtime.getRuntime().exit(0)
            }
        })
    }
}
