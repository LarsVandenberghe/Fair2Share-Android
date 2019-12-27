package com.example.fair2share.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.example.fair2share.R

class ActivityFragment : Fragment() {
    private lateinit var viewModel: ActivityFragmentViewModel
    private lateinit var viewModelFactory: ActivityFragmentViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = ActivityFragmentViewModelFactory(arguments!!.getLong("activityId")!!)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ActivityFragmentViewModel::class.java)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_activity, container, false)
    }

}
