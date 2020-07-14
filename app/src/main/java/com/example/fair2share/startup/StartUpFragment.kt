package com.example.fair2share.startup

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.os.Handler
import android.util.AttributeSet
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.fair2share.R

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

        val sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
        viewModelFactory = StartUpViewModelFactory(sharedPrefs)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartUpViewModel::class.java)

        if (sharedPrefs != null){
            Toast.makeText(requireContext(), "has resource", Toast.LENGTH_LONG).show()
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