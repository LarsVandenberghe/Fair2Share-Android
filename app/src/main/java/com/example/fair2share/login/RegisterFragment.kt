package com.example.fair2share.login

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentLoginBinding
import com.example.fair2share.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentRegisterBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }
}
