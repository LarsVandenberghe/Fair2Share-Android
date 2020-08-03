package com.example.fair2share.startup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.fair2share.LoginActivity
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi

class StartUpFragment : Fragment() {

    private lateinit var viewModel: StartUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AccountApi.sharedPreferences = requireActivity()
            .getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)

        val viewModelFactory = StartUpViewModelFactory(AccountApi.sharedPreferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartUpViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        if (viewModel.token.value != null){
            viewModel.getProfile()

            viewModel.shouldRelog.observe(this, Observer {
                if (it){
                    handleLoginState()
                }
            })

            viewModel.profile.observe(this, Observer {
                navigateToMainActivity(it.makeDTO())
            })
        } else {
            handleLoginState()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    private fun handleLoginState(){
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }, 1000)
    }

    private fun navigateToMainActivity(profile: ProfileDTOProperty){
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("profile", profile)
        startActivity(intent)
        requireActivity().finish()
    }
}