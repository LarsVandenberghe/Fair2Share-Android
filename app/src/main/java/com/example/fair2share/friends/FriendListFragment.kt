package com.example.fair2share.friends

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fair2share.R
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.databinding.FragmentFriendListBinding

class FriendListFragment : Fragment() {
    private lateinit var viewModel: FriendsViewModel
    private lateinit var viewModelFactory: FriendsViewModelFactory
    private lateinit var friendRequestAdapter: FriendRequestBindingAdapter
    private lateinit var friendsAdapter: FriendBindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var friends : List<ProfileProperty>? = arguments?.getParcelableArrayList("friends")
        viewModelFactory = FriendsViewModelFactory(friends)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendsViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentFriendListBinding>(inflater, R.layout.fragment_friend_list, container, false)
        configureAdapters(binding)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.succes.observe(this, Observer {
            if (it){
                viewModel.update()
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: View = requireView().findViewById(R.id.fabAddFriend)

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_friendListFragment_to_addFriendFragment)
        }
    }


    private fun configureAdapters(binding: FragmentFriendListBinding) {
        friendRequestAdapter = FriendRequestBindingAdapter(viewModel)
        friendsAdapter = FriendBindingAdapter()

        viewModel.friendRequests.observe(this, Observer {
            friendRequestAdapter.data = it
            if (it.size == 0){
                requireView().findViewById<TextView>(R.id.txtNoFriendRequests).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txtNoFriendRequests).visibility = View.GONE
            }

        })

        viewModel.friends.observe(this, Observer {
            friendsAdapter.data = it
            if (it.size == 0){
                requireView().findViewById<TextView>(R.id.txtNoFriends).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txtNoFriends).visibility = View.GONE
            }
        })

        binding.lstFriendsRequests.adapter = friendRequestAdapter
        binding.lstFriends.adapter = friendsAdapter
    }
}