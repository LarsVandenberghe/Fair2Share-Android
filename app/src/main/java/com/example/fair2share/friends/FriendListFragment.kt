package com.example.fair2share.friends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentActivityBinding

class FriendListFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // val binding = DataBindingUtil.inflate<Fragment>(inflater, R.layout.fragment_friend_list, container, false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: View = requireView().findViewById(R.id.fabAddFriend)
        fab.setOnClickListener {
            findNavController().navigate(R.id.action_friendListFragment_to_addFriendFragment)
        }
    }
}