package com.example.fair2share.friends

import android.content.Context
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
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.databinding.FragmentAddfriendBinding


class AddFriendFragment : Fragment() {
    private lateinit var viewModel: AddFriendViewModel
    private val safeArgs : AddFriendFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddfriendBinding>(inflater, R.layout.fragment_addfriend, container, false)
        bindViewModelData(binding)
        return binding.root
    }

    private fun makeViewModel(){
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = AddFriendViewModelFactory(safeArgs.email, database)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFriendViewModel::class.java)
    }

    private fun setupObservables(){
        viewModel.errorMessage.observe(this, Observer {message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        viewModel.succes.observe(this, Observer {
            if (it){
                Toast.makeText(context, getString(R.string.fragment_addfriend_success), Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })
    }

    private fun bindViewModelData(binding: FragmentAddfriendBinding){
        binding.btnRecycleraddandremovefriendAddfriend.setOnClickListener {
            viewModel.addFriendByEmail(binding.editAddfriendEmail.text.toString(), resources)
        }

        binding.constraintlayoutAddfriend.setOnClickListener{ view: View ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

    }
}