package com.example.fair2share.activity.summary

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.databinding.FragmentActivitySummaryBinding


class ActivitySummaryFragment : Fragment() {

    private lateinit var viewModel: ActivitySummaryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            val viewModelFactory =
                ActivityFragmentViewModelFactory(it.getParcelable("activity")!!)
            viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivitySummaryViewModel::class.java)
        }
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentActivitySummaryBinding>(inflater, R.layout.fragment_activity_summary, container, false)

        val summaryAdapter =
            SummaryBindingAdapter(viewModel)
        binding.rvSummaryList.adapter = summaryAdapter

        viewModel.summary.observe(this, Observer { summaryItem ->
            summaryAdapter.data = summaryItem
        })
        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.activity_summary_overflow_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btnSummaryOverflowTransactions -> {
                findNavController().navigateUp()
                return true
            }
            R.id.btnSummaryOverflowAddFriends -> {
                findNavController().navigate(R.id.action_activitySummaryFragment_to_managePeopleInActivityFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
