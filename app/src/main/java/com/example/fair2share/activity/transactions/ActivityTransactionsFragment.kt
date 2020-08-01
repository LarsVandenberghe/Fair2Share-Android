package com.example.fair2share.activity.transactions

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.databinding.FragmentActivitytransactionsBinding

class ActivityTransactionsFragment : Fragment() {
    private lateinit var viewModel: ActivityTransactionsFragmentViewModel
    private val safeArgs: ActivityTransactionsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory = ActivityFragmentViewModelFactory(safeArgs.activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(
            ActivityTransactionsFragmentViewModel::class.java
        )

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

        val transactionAdapter = TransactionBindingAdapter(viewModel)

        binding.rvActivitytransactionsList.adapter = transactionAdapter

        viewModel.transactions.observe(viewLifecycleOwner, Observer { transactions ->
            if(transactions.size == 0){
                binding.txtActivitytransactionsNotransactions.visibility = View.VISIBLE
            } else {
                binding.txtActivitytransactionsNotransactions.visibility = View.GONE
            }
            transactionAdapter.data = transactions.reversed()
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it){
                findNavController().navigateUp()
            }
        })

        binding.fabActivitytransactionsAddactivity.setOnClickListener {
            val action = ActivityTransactionsFragmentDirections.actionActivityTransactionsFragmentToAddEditTransactionFragment(
                null,
                viewModel.activity
            )
            findNavController().navigate(action)
        }

        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_activitytransactionsoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_transactionsoverflow_removeactivity -> {
                viewModel.removeActivity()
                return true
            }
            R.id.btn_transactionsoverflow_summary -> {
                val action = ActivityTransactionsFragmentDirections
                    .actionActivityTransactionsFragmentToActivitySummaryFragment(viewModel.activity)
                findNavController().navigate(action)
                return true
            }
            R.id.btn_transactionsoverflow_editactivity -> {
                val action = ActivityTransactionsFragmentDirections
                    .actionActivityTransactionsFragmentToAddEditActivityFragment(viewModel.activity)
                findNavController().navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
