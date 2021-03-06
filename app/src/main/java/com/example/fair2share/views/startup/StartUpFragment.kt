package com.example.fair2share.views.startup

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fair2share.LoginActivity
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.viewmodels.DatabaseOnlyViewModelFactory
import com.example.fair2share.viewmodels.startup.StartUpViewModel

class StartUpFragment : Fragment() {

    private lateinit var viewModel: StartUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_startup, container, false)
    }

    private fun handleLoginState() {
        val handler = Handler()
        handler.postDelayed({
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }, 1000)
    }

    private fun navigateToMainActivity(profile: ProfileDTOProperty) {
        val intent = Intent(context, MainActivity::class.java)
        intent.putExtra("profile", profile)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun makeViewModel() {
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory =
            DatabaseOnlyViewModelFactory(
                database
            )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(StartUpViewModel::class.java)
    }

    private fun setupObservables() {
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        if (viewModel.token.value != null) {
            viewModel.getProfileOnline(resources)

            viewModel.shouldRelog.observe(this, Observer {
                if (it) {
                    handleLoginState()
                }
            })

            viewModel.isOffline.observe(this, Observer {
                if (it) {
                    viewModel.getProfileCached()
                }
            })

            viewModel.profile.observe(this, Observer {
                navigateToMainActivity(it)
            })
        } else {
            handleLoginState()
        }
    }
}