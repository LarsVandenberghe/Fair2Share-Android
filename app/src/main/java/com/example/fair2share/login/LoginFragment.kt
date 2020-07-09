package com.example.fair2share.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private  lateinit var viewModelFactory: LoginViewModelFactory
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val sharedPrefs = activity?.getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
        sharedPrefs?.let {
            viewModelFactory = LoginViewModelFactory(it)
            viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

            binding.btnLogin.setOnClickListener{view: View ->
                viewModel.login(binding.txtLoginEmail.text.toString(), binding.txtLoginPassword.text.toString())
            }

            viewModel.loggedIn.observe(this, Observer { bool ->
                if (bool){
                    startActivity(Intent(context, MainActivity::class.java))
                }
            })

            viewModel.errorMessage.observe(this, Observer { message  ->
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            })

            binding.loginPane.setOnClickListener{ view: View ->
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }

        }
    }

}
