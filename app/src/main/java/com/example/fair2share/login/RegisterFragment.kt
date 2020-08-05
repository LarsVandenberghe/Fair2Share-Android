package com.example.fair2share.login

import android.app.Activity
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

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel =
            ViewModelProviders.of(this).get(RegisterViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        viewModel.loggedIn.observe(this, Observer { isLoggedIn ->
            if (isLoggedIn){
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        binding.registerData = viewModel.registerData

        binding.btnRegisterRegister.setOnClickListener {
            viewModel.register()
        }

        binding.linearlayoutRegister.setOnClickListener{ view: View ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        return binding.root
    }
}
