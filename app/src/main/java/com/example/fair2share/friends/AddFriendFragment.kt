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
import com.example.fair2share.databinding.FragmentAddFriendBinding

class AddFriendFragment : Fragment() {
    private lateinit var viewModel: AddFriendViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val viewModelFactory = AddFriendViewModelFactory(it.getString("email")!!)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddFriendViewModel::class.java)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddFriendBinding>(inflater, R.layout.fragment_add_friend, container, false)

        binding.addFriend.setOnClickListener {
            viewModel.addFriendByEmail(binding.txtAddEmailAddress.text.toString())
        }

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.succes.observe(this, Observer {
            if (it){
                //TODO: Stringify
                Toast.makeText(context, "FriendRequest has been sent.", Toast.LENGTH_LONG).show()
                findNavController().navigateUp()
            }
        })

        return binding.root
    }
}