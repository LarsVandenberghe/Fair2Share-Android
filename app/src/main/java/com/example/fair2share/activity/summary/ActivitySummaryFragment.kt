package com.example.fair2share.activity.summary

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.databinding.FragmentActivitysummaryBinding


class ActivitySummaryFragment : Fragment() {

    private lateinit var viewModel: ActivitySummaryViewModel
    private val safeArgs: ActivitySummaryFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory =
            ActivityFragmentViewModelFactory(safeArgs.activity)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivitySummaryViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentActivitysummaryBinding>(inflater, R.layout.fragment_activitysummary, container, false)

        val summaryAdapter =
            SummaryBindingAdapter(viewModel)
        binding.rvActivitySummarylist.adapter = summaryAdapter

        viewModel.summary.observe(viewLifecycleOwner, Observer { summaryItem ->
            summaryAdapter.data = summaryItem
        })
        viewModel.update()

        (activity as AppCompatActivity).supportActionBar?.title = viewModel.activity.name
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_activitysummaryoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_summaryoverflow_transactions -> {
                findNavController().navigateUp()
                return true
            }
            R.id.btn_summaryoverflow_addfriends -> {
                val action = ActivitySummaryFragmentDirections.actionActivitySummaryFragmentToManagePeopleInActivityFragment(viewModel.activity)
                findNavController().navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
