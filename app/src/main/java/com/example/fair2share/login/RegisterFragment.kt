package com.example.fair2share.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
        sharedPrefs?.let {
            val viewModelFactory = LoginAndRegisterViewModelFactory(it)
            viewModel =
                ViewModelProviders.of(this, viewModelFactory).get(RegisterViewModel::class.java)

            viewModel.errorMessage.observe(this, Observer { message ->
                Toast.makeText(context, message, Toast.LENGTH_LONG).show()
            })

            viewModel.loggedIn.observe(viewLifecycleOwner, Observer { isLoggedIn ->
                if (isLoggedIn){
                    startActivity(Intent(context, MainActivity::class.java))
                    requireActivity().finish()
                }
            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        binding.btnRegisterRegister.setOnClickListener {
            viewModel.register(
                binding.editRegisterEmail.text.toString(),
                binding.editRegisterPassword.text.toString(),
                binding.editRegisterFirstname.text.toString(),
                binding.editRegisterLastname.text.toString(),
                binding.editRegisterConfirmpassword.text.toString()
            )
        }

        return binding.root
    }
}
