package com.example.fair2share.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentFriendListBinding
import com.example.fair2share.databinding.FragmentProfileBinding
import com.example.fair2share.profile.ActivityBindingAdapter

class FriendListFragment : Fragment() {
    private lateinit var viewModel: FriendRequestViewModel
   //private lateinit var binding: FragmentFriendListBinding
    private lateinit var adapter: FriendBindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FriendRequestViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentFriendListBinding>(inflater, R.layout.fragment_friend_list, container, false)

        //binding.
        adapter = FriendBindingAdapter()
        viewModel.profile.observe(this, Observer {
            adapter.data = it
        })
        binding.lstFriends.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: View = requireView().findViewById(R.id.fabAddFriend)



        fab.setOnClickListener {
            findNavController().navigate(R.id.action_friendListFragment_to_addFriendFragment)
        }
    }
}