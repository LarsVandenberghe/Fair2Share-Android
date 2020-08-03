package com.example.fair2share.activity.people

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.databinding.FragmentManagepeopleinactivityBinding

class ManagePeopleInActivityFragment : Fragment() {
    private lateinit var viewModel: ManagePeopleInActivityViewModel
    private val safeArgs: ManagePeopleInActivityFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ActivityFragmentViewModelFactory(safeArgs.activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ManagePeopleInActivityViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding
                = DataBindingUtil.inflate<FragmentManagepeopleinactivityBinding>(
            inflater,
            R.layout.fragment_managepeopleinactivity,
            container,
            false
        )
        configureAdapters(binding)

        binding.btnManagepeopleinactivityConfirm.setOnClickListener {
            viewModel.confirm()
        }

        viewModel.success.observe(viewLifecycleOwner, Observer {
            findNavController().navigateUp()
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        return binding.root
    }

    private fun configureAdapters(binding: FragmentManagepeopleinactivityBinding) {
        val candidateAdapter = ActivityCandidatesAdapter(viewModel)
        val participantAdapter = ActivityParticipantsAdapter(viewModel)

        viewModel.candidates.observe(viewLifecycleOwner, Observer {
            candidateAdapter.data = it
            if (it.isEmpty()){
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_nocandidates).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_nocandidates).visibility = View.GONE
            }

        })

        viewModel.participants.observe(viewLifecycleOwner, Observer {
            participantAdapter.data = it
            if (it.isEmpty()){
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_noparticipants).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_noparticipants).visibility = View.GONE
            }
        })

        binding.rvManagepeopleinactivityCandidates.adapter = candidateAdapter
        binding.rvManagepeopleinactivityParticipants.adapter = participantAdapter
    }

    override fun onDestroyView() {
        viewModel.resetSelected()
        super.onDestroyView()
    }
}