package com.example.fair2share.views.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentRegisterBinding
import com.example.fair2share.viewmodels.login.RegisterViewModel

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding: FragmentRegisterBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_register,
            container,
            false
        )

        bindViewModelData(binding)
        return binding.root
    }

    private fun makeViewModel() {
        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
    }

    private fun setupObservables() {
        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        viewModel.loggedIn.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn) {
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    private fun bindViewModelData(binding: FragmentRegisterBinding) {
        binding.registerData = viewModel.registerData
        binding.btnRegisterRegister.setOnClickListener {
            viewModel.register(resources)
        }
        binding.linearlayoutRegister.setOnClickListener { view: View ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
