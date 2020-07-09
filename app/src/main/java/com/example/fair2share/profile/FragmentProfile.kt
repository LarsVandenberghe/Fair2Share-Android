package com.example.fair2share.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentProfileBinding
import com.google.android.material.navigation.NavigationView


class FragmentProfile : Fragment() {

    private lateinit var viewModel: FragmentProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ActivityBindingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentProfileViewModel::class.java)
        viewModel.profile.observe(this, Observer{ data ->
            binding.profile = data
            data.activities?.let{
                adapter.data = it
            }
            addFriendRequests(data.amountOfFriendRequests ?: 0)
        })
        (activity as MainActivity).bindProfileToNavHeader(viewModel)

        viewModel.errorMessage.observe(this, Observer { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        })
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        adapter = ActivityBindingAdapter(viewModel)
        binding.activityList.adapter = adapter
        viewModel.update()
        binding.button.setOnClickListener{
            navigateToCreateActivity()
        }
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.fragment_profile_title)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.add_activity_btn -> {
                navigateToCreateActivity()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToCreateActivity(){
        findNavController().navigate(R.id.action_fragmentProfile_to_createActivityFragment)
    }

    private fun addFriendRequests(amountOfFriendRequests: Int){
        val menu = requireActivity().findViewById<NavigationView>(R.id.navView).menu
        if (amountOfFriendRequests > 0){
            menu.findItem(R.id.btnMenuFriends).title = String.format(
                "%s (%d)",
                "Friends",
                amountOfFriendRequests
            )
        } else {
            menu.findItem(R.id.btnMenuFriends).title = String.format("%s", "Friends")
        }
    }
}
