package com.example.fair2share.profile

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.database.DatabaseOnlyViewModelFactory
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.databinding.FragmentProfileBinding
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.google.android.material.navigation.NavigationView


class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileFragmentViewModel
    private var firstLoad : Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
        setHasOptionsMenu(true)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).bindProfileToNavHeader(viewModel)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, container, false)

        val adapter = bindViewModelData(binding)
        setupUIObservables(binding, adapter)
        receiveProfileData()

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

    private fun makeViewModel(){
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = DatabaseOnlyViewModelFactory(database)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ProfileFragmentViewModel::class.java)
    }

    private fun setupObservables(){
        viewModel.errorMessage.observe(this, Observer { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        })

        viewModel.activityErrorMessage.observe(this, Observer { errorMsg ->
            Toast.makeText(context, errorMsg, Toast.LENGTH_SHORT).show()
        })

        viewModel.activityDeleteSuccess.observe(this, Observer {
            if(it){
                viewModel.update(resources)
            }
        })
    }

    private fun setupUIObservables(binding: FragmentProfileBinding, adapter: ActivityBindingAdapter){
        viewModel.profile.observe(viewLifecycleOwner, Observer{ data ->
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

    private fun bindViewModelData(binding: FragmentProfileBinding): ActivityBindingAdapter {
        val adapter = ActivityBindingAdapter(viewModel)
        binding.rvProfileActivitylist.adapter = adapter

        binding.fabProfileAddactivity.setOnClickListener{
            navigateToCreateActivity()
        }

        binding.refreshlayoutProfile.setOnRefreshListener{
            viewModel.update(resources)
            binding.refreshlayoutProfile.setRefreshing(false)
        }
        return adapter
    }

    private fun receiveProfileData() {
        val profile = requireActivity().intent.getParcelableExtra<ProfileDTOProperty>("profile")

        if (!firstLoad || profile == null){
            viewModel.update(resources)
        } else {
            viewModel.update(resources, profile)
            firstLoad = false
        }
    }
}
