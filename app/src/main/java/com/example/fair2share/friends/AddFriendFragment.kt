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
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentAddfriendBinding


class AddFriendFragment : Fragment() {
    private lateinit var viewModel: AddFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("email")?.let {
            val viewModelFactory = AddFriendViewModelFactory(it)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFriendViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddfriendBinding>(inflater, R.layout.fragment_addfriend, container, false)

        binding.btnRecycleraddandremovefriendAddfriend.setOnClickListener {
            viewModel.addFriendByEmail(binding.editAddfriendEmail.text.toString())
        }

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.succes.observe(viewLifecycleOwner, Observer {
            if (it){
                //TODO: Stringify
                Toast.makeText(context, "FriendRequest has been sent.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

        return binding.root
    }
}