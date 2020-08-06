package com.example.fair2share.activity.summary

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
import com.example.fair2share.Utils
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.databinding.FragmentActivitysummaryBinding


class ActivitySummaryFragment : Fragment() {

    private lateinit var viewModel: ActivitySummaryViewModel
    private val safeArgs: ActivitySummaryFragmentArgs by navArgs()

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
        val binding = DataBindingUtil.inflate<FragmentActivitysummaryBinding>(
            inflater,
            R.layout.fragment_activitysummary,
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
        inflater.inflate(R.menu.menu_activitysummaryoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_summaryoverflow_transactions -> {
                findNavController().navigateUp()
                return true
            }
            R.id.btn_summaryoverflow_addfriends -> {
                val action =
                    ActivitySummaryFragmentDirections.actionActivitySummaryFragmentToManagePeopleInActivityFragment(
                        viewModel.activityArg
                    )
                findNavController().navigate(action)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun makeViewModel() {
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory =
            ActivityFragmentViewModelFactory(safeArgs.activity, database)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(ActivitySummaryViewModel::class.java)

    }

    private fun setupObservables() {
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun configureAdapters(binding: FragmentActivitysummaryBinding) {
        val summaryAdapter =
            SummaryBindingAdapter(viewModel)
        binding.rvActivitySummarylist.adapter = summaryAdapter

        viewModel.summary.observe(viewLifecycleOwner, Observer { summaryItem ->
            summaryAdapter.data = summaryItem
        })

        binding.refreshlayoutActivity.setOnRefreshListener {
            viewModel.update(resources)
            Utils.stopRefreshingAnimationAfter700ms(binding.refreshlayoutActivity)
        }
    }
}
