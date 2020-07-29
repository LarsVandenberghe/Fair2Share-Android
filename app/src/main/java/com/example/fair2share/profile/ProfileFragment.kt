package com.example.fair2share.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.databinding.FragmentProfileBinding
import com.google.android.material.navigation.NavigationView


class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileFragmentViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ActivityBindingAdapter
    var firstLoad : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        })

        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).bindProfileToNavHeader(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        adapter = ActivityBindingAdapter(viewModel)
        binding.rvProfileActivityList.adapter = adapter

        receiveProfileData()

        binding.fabProfileAddActivity.setOnClickListener{
            navigateToCreateActivity()
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_profile_overflow_add_activity -> {
                navigateToCreateActivity()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToCreateActivity(){
        findNavController().navigate(R.id.action_fragmentProfile_to_createActivityFragment)
    }

    private fun addFriendRequests(amountOfFriendRequests: Int){
        val menu = requireActivity().findViewById<NavigationView>(R.id.nav_view_activity_main).menu
        if (amountOfFriendRequests > 0){
            menu.findItem(R.id.btn_navdrawer_friends).title = String.format(
                "Friends (%d)",
                amountOfFriendRequests
            )
        } else {
            menu.findItem(R.id.btn_navdrawer_friends).title = "Friends"
        }
    }

    private fun receiveProfileData() {
        val profile = requireActivity().intent.getParcelableExtra<ProfileProperty>("profile")
        if (!firstLoad || profile == null){
            viewModel.update()
        } else {
            viewModel.update(profile)
            firstLoad = false
        }
        viewModel.profile.observe(this, Observer{ data ->
            binding.profile = data
            data.activities?.let{
                adapter.data = it
            }
            addFriendRequests(data.amountOfFriendRequests ?: 0)
        })
    }
}
