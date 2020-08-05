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
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.databinding.FragmentFriendlistBinding
import com.example.fair2share.models.dto_models.ProfileDTOProperty

class FriendListFragment : Fragment() {
    private lateinit var viewModel: FriendListViewModel
    private val safeArgs: FriendListFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val profile : ProfileDTOProperty = safeArgs.profile
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = FriendListViewModelFactory(profile, database)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(FriendListViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.success.observe(this, Observer {
            if (it){
                viewModel.update(resources)
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentFriendlistBinding>(inflater, R.layout.fragment_friendlist, container, false)
        configureAdapters(binding)
        viewModel.update(resources)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val fab: View = requireView().findViewById(R.id.fab_friendlst_addfriend)

        fab.setOnClickListener {
            val action = FriendListFragmentDirections.actionFriendListFragmentToAddFriendFragment(viewModel.profile.value!!.email!!)
            findNavController().navigate(action)
        }
    }


    private fun configureAdapters(binding: FragmentFriendlistBinding) {
        val friendRequestAdapter = FriendRequestBindingAdapter(viewModel)
        val friendsAdapter = FriendBindingAdapter()

        viewModel.friendRequests.observe(viewLifecycleOwner, Observer {
            friendRequestAdapter.data = it
            if (it.isEmpty()){
                requireView().findViewById<TextView>(R.id.txt_friendlst_nofriendrequests).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_friendlst_nofriendrequests).visibility = View.GONE
            }

        })

        viewModel.profile.observe(viewLifecycleOwner, Observer {
            val friends = it.friends ?: emptyList()
            friendsAdapter.data = friends
            if (friends.isEmpty()){
                requireView().findViewById<TextView>(R.id.txt_friendlst_nofriends).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_friendlst_nofriends).visibility = View.GONE
            }
        })

        binding.rvFriendlstFriendsrequests.adapter = friendRequestAdapter
        binding.rvFriendlstFriends.adapter = friendsAdapter
    }
}