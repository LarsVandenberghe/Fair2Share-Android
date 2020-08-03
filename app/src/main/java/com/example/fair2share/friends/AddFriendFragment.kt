package com.example.fair2share.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentAddfriendBinding


class AddFriendFragment : Fragment() {
    private lateinit var viewModel: AddFriendViewModel
    private val safeArgs : AddFriendFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = AddFriendViewModelFactory(safeArgs.email)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFriendViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {message ->
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        })

        viewModel.succes.observe(this, Observer {
            if (it){
                //TODO: Stringify
                Toast.makeText(context, "FriendRequest has been sent.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddfriendBinding>(inflater, R.layout.fragment_addfriend, container, false)

        binding.btnRecycleraddandremovefriendAddfriend.setOnClickListener {
            viewModel.addFriendByEmail(binding.editAddfriendEmail.text.toString())
        }

        return binding.root
    }
}