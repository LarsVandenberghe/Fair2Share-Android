package com.example.fair2share.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.LoginActivity
import com.example.fair2share.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private  lateinit var viewModelFactory: LoginViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: FragmentLoginBinding =  DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        val sharedPrefs = requireActivity().getSharedPreferences(getString(R.string.app_name), Activity.MODE_PRIVATE)
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
                    requireActivity().finish()
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

        (requireActivity() as LoginActivity).setIsOnLoginFragment(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.login_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.register_btn -> {
                requireActivity().findNavController(R.id.startUpNavHostFragment).navigate(R.id.action_loginFragment_to_registerFragment)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (requireActivity() as LoginActivity).setIsOnLoginFragment(false)
    }
}
