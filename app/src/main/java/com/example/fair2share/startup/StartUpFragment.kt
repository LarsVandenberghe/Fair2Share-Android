package com.example.fair2share.startup

import android.app.Activity
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val sharedPrefs = activity?.getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
        sharedPrefs?.let {
            viewModelFactory = StartUpViewModelFactory(it)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartUpViewModel::class.java)

        }
    }
}