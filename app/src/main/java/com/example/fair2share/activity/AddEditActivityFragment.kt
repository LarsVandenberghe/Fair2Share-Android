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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.Valutas
import com.example.fair2share.databinding.FragmentAddeditactivityBinding
import com.example.fair2share.models.dto_models.ActivityDTOProperty

class AddEditActivityFragment : Fragment() {
    private lateinit var viewModel : AddEditActivityFragmentViewModel
    private val safeArgs: AddEditActivityFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = if (safeArgs.activity != null){
            AddEditActivityFragmentViewModelFactory((safeArgs.activity as ActivityDTOProperty).makeDataModel())
        } else {
            AddEditActivityFragmentViewModelFactory(ActivityProperty.makeEmpty())
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditActivityFragmentViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })

        viewModel.navigate.observe(this, Observer {
            if (it){
                findNavController().navigateUp()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddeditactivityBinding>(inflater, R.layout.fragment_addeditactivity, container, false)

        binding.activity = viewModel.activity

        binding.cboAddeditactivityValuta.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, Valutas.values())

        binding.constraintlayoutAddeditactivity.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        binding.btnAddeditactivity.text = getButtonName()

        binding.btnAddeditactivity.setOnClickListener {
            viewModel.createOrUpdate()
        }

        return binding.root
    }

    private fun getButtonName() : String{
        return if (viewModel.isNewActivity){
            getString(R.string.btn_add)
        } else {
            getString(R.string.btn_edit)
        }
    }
}
