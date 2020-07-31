package com.example.fair2share.activity.transactions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.databinding.FragmentAddedittransactionBinding

class AddEditTransactionFragment : Fragment() {

    private lateinit var viewModel: AddEditTransactionViewModel
    private val safeArgs: AddEditTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(AddEditTransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddedittransactionBinding>(inflater, R.layout.fragment_addedittransaction, container, false)
        viewModel.transaction = safeArgs.transaction

        if (viewModel.transaction != null){

        }
        return binding.root
    }
}