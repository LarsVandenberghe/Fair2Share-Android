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
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.databinding.FragmentActivityTransactionsBinding

class ActivityTransactionsFragment : Fragment() {
    private lateinit var viewModel: ActivityTransactionsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            val viewModelFactory =
                ActivityFragmentViewModelFactory(it.getParcelable("activity")!!)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(
                ActivityTransactionsFragmentViewModel::class.java)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentActivityTransactionsBinding>(inflater, R.layout.fragment_activity_transactions, container, false)
        val transactionAdapter =
            TransactionBindingAdapter(viewModel)

        binding.rvActivityTransactionList.adapter = transactionAdapter

        viewModel.transactions.observe(this, Observer { transactions ->
            transactionAdapter.data = transactions.reversed()
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(this, Observer {
            if (it){
                findNavController().navigateUp()
            }
        })

        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_transactions_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_transactions_overflow_remove_activity -> {
                viewModel.removeActivity(viewModel.activity)
                return true
            }
            R.id.btn_transactions_overflow_summary -> {
                val bundle = Bundle()
                bundle.putParcelable("activity", viewModel.activity)
                findNavController().navigate(R.id.action_activityTransactionsFragment_to_activitySummaryFragment, bundle)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
