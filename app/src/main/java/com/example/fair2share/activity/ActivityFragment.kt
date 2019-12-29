package com.example.fair2share.activity

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
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.databinding.FragmentActivityBinding

class ActivityFragment : Fragment() {
    private lateinit var viewModel: ActivityFragmentViewModel
    private lateinit var viewModelFactory: ActivityFragmentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ActivityFragmentViewModelFactory(arguments?.getParcelable<ActivityProperty>("activity")!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivityFragmentViewModel::class.java)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentActivityBinding>(inflater, R.layout.fragment_activity, container, false)
        val transactionAdapter = TransactionBindingAdapter(viewModel)
        val summaryAdapter = SummaryBindingAdapter(viewModel)

        binding.transactionList.adapter = transactionAdapter
        binding.summaryList.adapter = summaryAdapter

        viewModel.transactions.observe(this, Observer { transactions ->
            transactionAdapter.data = transactions.reversed()
        })

        viewModel.summary.observe(this, Observer { summaryItem ->
            summaryAdapter.data = summaryItem
        })

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(this, Observer {
            if (it){
                findNavController().navigate(R.id.action_activityFragment_to_fragmentProfile)
            }
        })

        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.activity_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.remove_activity_btn -> {
                viewModel.removeActivity(viewModel.activity)
                return true
            } else -> super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

}
