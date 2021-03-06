package com.example.fair2share.views.activity.transactions.people

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
import com.example.fair2share.databinding.FragmentManagepeopleintransactionBinding
import com.example.fair2share.viewmodels.activity.transactions.people.ManagePeopleInTransactionViewModel
import com.example.fair2share.viewmodels.activity.transactions.people.ManagePeopleInTransactionViewModelFactory

class ManagePeopleInTransactionFragment : Fragment() {
    private lateinit var viewModel: ManagePeopleInTransactionViewModel
    private val safeArgs: ManagePeopleInTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentManagepeopleintransactionBinding>(
            inflater,
            R.layout.fragment_managepeopleintransaction,
            container,
            false
        )
        viewModel.update(resources)
        configureAdapters(binding)

        binding.btnManagepeopleintransactionConfirm.setOnClickListener {
            viewModel.confirm(resources)
        }

        return binding.root
    }

    private fun configureAdapters(binding: FragmentManagepeopleintransactionBinding) {
        val candidateAdapter = TransactionCandidatesAdapter(viewModel)
        val participantAdapter = TransactionParticipantsAdapter(viewModel)

        viewModel.candidates.observe(viewLifecycleOwner, Observer {
            candidateAdapter.data = it
            if (it.isEmpty()) {
                requireView().findViewById<TextView>(R.id.txt_managepeopleintransaction_nocandidates)
                    .visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_managepeopleintransaction_nocandidates)
                    .visibility = View.GONE
            }

        })

        viewModel.participants.observe(viewLifecycleOwner, Observer {
            participantAdapter.data = it
            if (it.isEmpty()) {
                requireView().findViewById<TextView>(R.id.txt_managepeopleintransaction_noparticipants)
                    .visibility = View.VISIBLE
            } else {
                requireView().findViewById<TextView>(R.id.txt_managepeopleintransaction_noparticipants)
                    .visibility = View.GONE
            }
        })

        binding.rvMmanagepeopleintransactionCandidates.adapter = candidateAdapter
        binding.rvManagepeopleintransactionParticipants.adapter = participantAdapter
    }

    override fun onDestroyView() {
        viewModel.resetSelected()
        super.onDestroyView()
    }

    private fun makeViewModel() {
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = ManagePeopleInTransactionViewModelFactory(
            safeArgs.activity,
            safeArgs.transaction,
            database
        )
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            ManagePeopleInTransactionViewModel::class.java
        )
    }

    private fun setupObservables() {
        viewModel.success.observe(this, Observer {
            findNavController().navigateUp()
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }
}