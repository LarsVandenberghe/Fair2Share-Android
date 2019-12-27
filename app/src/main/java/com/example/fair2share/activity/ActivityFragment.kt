package com.example.fair2share.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.fair2share.R
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private lateinit var viewModel: ActivityFragmentViewModel
    private lateinit var viewModelFactory: ActivityFragmentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ActivityFragmentViewModelFactory(arguments?.getParcelable<ActivityProperty>("activity")!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivityFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentActivityBinding>(inflater, R.layout.fragment_activity, container, false)
        val transactionAdapter = TransactionBindingAdapter(viewModel.activity.currencyType)
        val summaryAdapter = SummaryBindingAdapter(viewModel.activity.currencyType)

        binding.transactionList.adapter = transactionAdapter
        binding.summaryList.adapter = summaryAdapter

        viewModel.transactions.observe(this, Observer { transactions ->
            transactionAdapter.data = transactions.reversed()
        })

        viewModel.summary.observe(this, Observer { summaryItem ->
            summaryAdapter.data = summaryItem
        })
        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }

}
