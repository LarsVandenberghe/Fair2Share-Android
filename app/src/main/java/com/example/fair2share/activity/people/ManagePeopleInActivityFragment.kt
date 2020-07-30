package com.example.fair2share.activity.people

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.databinding.FragmentManagepeopleinactivityBinding

class ManagePeopleInActivityFragment : Fragment() {
    private lateinit var viewModel: ManagePeopleInActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getParcelable<ActivityProperty>("activity")?.let{
            val viewModelFactory = ActivityFragmentViewModelFactory(it)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ManagePeopleInActivityViewModel::class.java)
        }
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
        return binding.root
    }

    private fun configureAdapters(binding: FragmentManagepeopleinactivityBinding) {
        val candidateAdapter = ActivityCandidatesAdapter(viewModel)
        val participantAdapter = ActivityParticipantsAdapter(viewModel)

        viewModel.candidates.observe(this, Observer {
            candidateAdapter.data = it
            if (it.size == 0){
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_nocandidates).visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_managepeopleinactivity_nocandidates).visibility = View.GONE
            }

        })

        viewModel.participants.observe(this, Observer {
            participantAdapter.data = it
            if (it.size == 0){
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