package com.example.fair2share.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.fair2share.LoginActivity
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
        setHasOptionsMenu(true)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        bindViewModelData(binding)

        (requireActivity() as LoginActivity).setIsOnLoginFragment(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_loginoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_register_register -> {
                val action = LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
                requireActivity().findNavController(R.id.navhostfragment_login).navigate(action)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as LoginActivity).setIsOnLoginFragment(false)
    }

    private fun makeViewModel(){
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
    }

    private fun setupObservables(){
        viewModel.errorMessage.observe(this, Observer { message  ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.loggedIn.observe(this, Observer { bool ->
            if (bool){
                startActivity(Intent(context, MainActivity::class.java))
                requireActivity().finish()
            }
        })

        viewModel.errorMessage.observe(this, Observer { message  ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun bindViewModelData(binding: FragmentLoginBinding) {
        binding.loginData = viewModel.loginData

        binding.btnLoginLogin.setOnClickListener{view: View ->
            viewModel.login(resources)
        }

        binding.constraintlayoutLogin.setOnClickListener{ view: View ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
