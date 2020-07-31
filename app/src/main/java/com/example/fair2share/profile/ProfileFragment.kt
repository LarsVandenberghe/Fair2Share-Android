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
    var firstLoad : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).bindProfileToNavHeader(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)
        val adapter = ActivityBindingAdapter(viewModel)
        binding.rvProfileActivitylist.adapter = adapter
        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        })

        receiveProfileData(binding, adapter)

        binding.fabProfileAddactivity.setOnClickListener{
            navigateToCreateActivity()
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_profileoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_profileoverflow_addactivity -> {
                navigateToCreateActivity()
                return true
            } else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToCreateActivity(){
        val action = ProfileFragmentDirections.actionFragmentProfileToAddEditActivityFragment()
        findNavController().navigate(action)
    }

    private fun addFriendRequests(amountOfFriendRequests: Int){
        val menu = requireActivity().findViewById<NavigationView>(R.id.navview_activity_main).menu
        if (amountOfFriendRequests > 0){
            menu.findItem(R.id.btn_navdrawer_friends).title = String.format(
                "Friends (%d)",
                amountOfFriendRequests
            )
        } else {
            menu.findItem(R.id.btn_navdrawer_friends).title = "Friends"
        }
    }

    private fun receiveProfileData(binding: FragmentProfileBinding, adapter: ActivityBindingAdapter) {
        val profile = requireActivity().intent.getParcelableExtra<ProfileProperty>("profile")

        if (!firstLoad || profile == null){
            viewModel.update()
        } else {
            viewModel.update(profile)
            firstLoad = false
        }

        viewModel.profile.observe(viewLifecycleOwner, Observer{ data ->
            binding.profile = data
            data.activities?.let{
                if (it.size == 0){
                    binding.txtProfileNoactivities.visibility = View.VISIBLE
                } else {
                    binding.txtProfileNoactivities.visibility = View.GONE
                }
                adapter.data = it
            }
            addFriendRequests(data.amountOfFriendRequests ?: 0)
        })
    }
}
