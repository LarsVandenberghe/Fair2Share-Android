package com.example.fair2share.activity.people

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fair2share.R

class ManagePeopleInActivityFragment : Fragment() {

    companion object {
        fun newInstance() =
            ManagePeopleInActivityFragment()
    }

    private lateinit var viewModel: ManagePeopleInActivityViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_manage_people_in_activity, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ManagePeopleInActivityViewModel::class.java)
        // TODO: Use the ViewModel
    }

}