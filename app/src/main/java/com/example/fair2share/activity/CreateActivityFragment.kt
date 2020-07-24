package com.example.fair2share.activity

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.fair2share.R
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.Valutas
import com.example.fair2share.databinding.FragmentCreateActivityBinding

class CreateActivityFragment : Fragment() {
    private lateinit var viewModel : CreateActivityFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(CreateActivityFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentCreateActivityBinding>(inflater, R.layout.fragment_create_activity, container, false)
        //(activity as AppCompatActivity).supportActionBar?.title = getString(R.string.fragment_create_activity_title)
        binding.fragmentCreateActivityValutaCbo.adapter = ArrayAdapter<Valutas>(context!!, R.layout.simple_text_view_item, Valutas.values())
        binding.createActivityPane.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        binding.fragmentCreateActivityAddBtn.setOnClickListener {
            val act = ActivityProperty(
                null,
                binding.fragmentCreateActivityName.text.toString(),
                binding.fragmentCreateActivityDescription.text.toString(),
                binding.fragmentCreateActivityValutaCbo.selectedItemPosition, null, null
            )
            viewModel.create(act)
        }
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(this, Observer {
            if (it){
                findNavController().navigate(R.id.action_createActivityFragment_to_fragmentProfile)
            }
        })

        return binding.root
    }
}
