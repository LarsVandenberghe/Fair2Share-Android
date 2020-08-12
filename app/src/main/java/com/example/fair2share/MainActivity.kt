package com.example.fair2share

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
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
import com.example.fair2share.network.AccountApi
import com.example.fair2share.network.AuthInterceptor
import com.example.fair2share.utils.Utils
import com.example.fair2share.viewmodels.profile.ProfileFragmentViewModel
import com.example.fair2share.views.profile.ProfileFragmentDirections
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHeaderBinding: NavHeaderBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSharedPreferences()

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val navController = this.findNavController(R.id.navhostfragment_main)
        val drawerLayout = binding.drawerlayoutActivityMain

        setupNavigationDrawer(drawerLayout, navController)
        preventGestureIfNotOnStartDestination(drawerLayout, navController)
        setupOnTokenExpiredRestartApp()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navhostfragment_main)
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        menu?.findItem(R.id.btn_navdrawer_friends)?.title = String.format("(%d)", 1)
        return super.onPrepareOptionsMenu(menu)
    }

    //This code is being called from the root fragment of this activity (ProfileFragment and passes its viewModel)
    fun bindProfileToNavHeader(vm: ProfileFragmentViewModel) {
        navHeaderBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.nav_header,
            binding.navviewActivityMain,
            true
        )
        vm.profile.observe(this, Observer { data ->
            navHeaderBinding.profile = data

            Glide.with(navHeaderBinding.imgNavheaderProfile.context)
                .load(Utils.getProfilePicUrl(data.profileId))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user)
                ).into(navHeaderBinding.imgNavheaderProfile)
        })
    }

    private fun preventGestureIfNotOnStartDestination(
        drawerLayout: DrawerLayout,
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener { nc: NavController, nd: NavDestination, _: Bundle? ->
            if (nd.id == nc.graph.startDestination) {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            } else {
                drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }


    private fun setupNavigationDrawer(drawerLayout: DrawerLayout, navController: NavController) {

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupWithNavController(binding.navviewActivityMain, navController)
        setupNavigationListener(drawerLayout, navController)
    }

    private fun setupNavigationListener(drawerLayout: DrawerLayout, navController: NavController) {
        (drawerLayout.navview_activity_main as NavigationView).setNavigationItemSelectedListener {
            if (it.itemId == R.id.btn_navdrawer_logout) {
                AccountApi.logout()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            if (it.itemId == R.id.btn_navdrawer_friends) {
                val action = ProfileFragmentDirections
                    .actionFragmentProfileToFriendListFragment(
                        navHeaderBinding.profile!!
                    )

                navController.navigate(action)
            }
            return@setNavigationItemSelectedListener false
        }
    }

    private fun setupOnTokenExpiredRestartApp() {
        AuthInterceptor.shouldRestart.observe(this, Observer {
            if (it) {
                val intent = Intent(this.baseContext, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                this.startActivity(intent)
                this.finish()
                Runtime.getRuntime().exit(0)
            }
        })
    }

    private fun setSharedPreferences() {
        AccountApi.sharedPreferences =
            this.getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
    }
}
