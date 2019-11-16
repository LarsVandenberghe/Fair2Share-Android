package com.example.fair2share.login

import android.app.Activity
import android.content.Context
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
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.example.fair2share.R
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.databinding.FragmentLoginBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {

    private var _viewModelJob = Job()
    private val _coroutineScope = CoroutineScope(_viewModelJob + Dispatchers.Main)

    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.btnLogin.setOnClickListener{view: View ->
            _coroutineScope.launch {
                try {
                    viewModel.login(binding.txtLoginEmail.text.toString(), binding.txtLoginPassword.text.toString())
                    view.findNavController().navigate(R.id.action_loginFragment_to_fragmentProfile)
                } catch (t : Throwable){
                    //show error on screen
                    Log.e("LoginFragment", t.message)
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginPane.setOnClickListener{ view: View ->
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
