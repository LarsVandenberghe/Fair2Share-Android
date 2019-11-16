package com.example.fair2share.login

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fair2share.R
import com.example.fair2share.data_models.LoginProperty
import com.example.fair2share.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

//    companion object {
//        fun newInstance() = LoginFragment()
//    }


    private lateinit var viewModel: LoginViewModel
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.btnLogin.setOnClickListener({event ->
            viewModel.login(binding.txtLoginEmail.text.toString(), binding.txtLoginPassword.text.toString())
        })
    }

}
