package com.example.fair2share.profile

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fair2share.MainActivity
import com.example.fair2share.R
import com.example.fair2share.databinding.FragmentProfileBinding

class FragmentProfile : Fragment() {

    private lateinit var viewModel: FragmentProfileViewModel
    private lateinit var binding: FragmentProfileBinding
    private lateinit var adapter: ActivityBindingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)
        adapter = ActivityBindingAdapter()
        binding.activityList.adapter = adapter
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(FragmentProfileViewModel::class.java)
        viewModel.profile.observe(this, Observer{ data ->

            binding.profile = data
            data.activities?.let{
                adapter.data = it
            }
        })

        (activity as MainActivity).bindProfileToNavHeader(viewModel)

    }
}
