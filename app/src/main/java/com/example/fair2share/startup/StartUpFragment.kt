package com.example.fair2share.startup

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.network.AccountApi

class StartUpFragment : Fragment() {

    private lateinit var viewModel: StartUpViewModel
    private lateinit var viewModelFactory: StartUpViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_start_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        AccountApi.sharedPreferences = requireActivity()
            .getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)

        viewModelFactory = StartUpViewModelFactory(AccountApi.sharedPreferences)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartUpViewModel::class.java)

        if (viewModel.token.value != null){
            viewModel.getProfile()
            viewModel.errorMessage.observe(this, Observer {
                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
            })

            viewModel.shouldRelog.observe(this, Observer {
                if (it){
                    handleLoginState()
                }
            })

            viewModel.profile.observe(this, Observer {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("profile", it)
                startActivity(intent)
                activity?.finish()
            })
        } else {
            val handler = Handler()
            handler.postDelayed({
                this.handleLoginState()
            }, 1000)
        }
    }

    private fun handleLoginState(){
        val navController = requireActivity().findNavController(R.id.startUpNavHostFragment)
        navController.navigate(R.id.action_startUpFragment_to_loginFragment)
    }
}