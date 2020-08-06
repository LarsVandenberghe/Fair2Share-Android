package com.example.fair2share.activity.transactions

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.databinding.FragmentActivitytransactionsBinding

class ActivityTransactionsFragment : Fragment() {
    private lateinit var viewModel: ActivityTransactionsFragmentViewModel
    private val safeArgs: ActivityTransactionsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            DataBindingUtil.inflate<FragmentActivitytransactionsBinding>(
                inflater,
                R.layout.fragment_activitytransactions,
                container,
                false
            )

        configureAdapters(binding)

        viewModel.update(resources)

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activityArg.name
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_activitytransactionsoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_transactionsoverflow_removeactivity -> {
                viewModel.removeActivity(resources)
                return true
            }
            R.id.btn_transactionsoverflow_summary -> {
                val action = ActivityTransactionsFragmentDirections
                    .actionActivityTransactionsFragmentToActivitySummaryFragment(viewModel.activityArg)
                findNavController().navigate(action)
                return true
            }
            R.id.btn_transactionsoverflow_editactivity -> {
                val action = ActivityTransactionsFragmentDirections
                    .actionActivityTransactionsFragmentToAddEditActivityFragment(viewModel.activityArg)
                findNavController().navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeViewModel(){
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = ActivityFragmentViewModelFactory(safeArgs.activity, database)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            ActivityTransactionsFragmentViewModel::class.java
        )
    }

    private fun setupObservables(){
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(this, Observer {
            if (it){
                findNavController().navigateUp()
            }
        })
    }

    private fun configureAdapters(binding: FragmentActivitytransactionsBinding){
        val transactionAdapter = TransactionBindingAdapter(viewModel)

        binding.rvActivitytransactionsList.adapter = transactionAdapter

        viewModel.activity.observe(viewLifecycleOwner, Observer { activity ->
            if(activity.transactions.isNullOrEmpty()){
                binding.txtActivitytransactionsNotransactions.visibility = View.VISIBLE
            } else {
                binding.txtActivitytransactionsNotransactions.visibility = View.GONE
            }
            transactionAdapter.data = activity.transactions?.reversed() ?: emptyList()
        })

        binding.fabActivitytransactionsAddactivity.setOnClickListener {
            val action = ActivityTransactionsFragmentDirections.actionActivityTransactionsFragmentToAddEditTransactionFragment(
                null,
                viewModel.activityArg
            )
            findNavController().navigate(action)
        }
    }
}
