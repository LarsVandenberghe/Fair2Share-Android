package com.example.fair2share.activity.transactions

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.databinding.FragmentAddedittransactionBinding
import com.example.fair2share.models.dto_models.TransactionDTOProperty

class AddEditTransactionFragment : Fragment() {

    private lateinit var viewModel: AddEditTransactionViewModel
    private val safeArgs: AddEditTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = if (safeArgs.transaction != null){
            setHasOptionsMenu(true)
            AddEditTransactionViewModelFactory(safeArgs.activity, (safeArgs.transaction as TransactionDTOProperty).makeDataModel(), false)
        } else {
            AddEditTransactionViewModelFactory(safeArgs.activity, TransactionProperty.makeEmpty(), true)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTransactionViewModel::class.java)

        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddedittransactionBinding>(inflater, R.layout.fragment_addedittransaction, container, false)

        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it){
                if (viewModel.isNewTransaction){
                    navigateAddPeopleOnNewTransaction()
                } else {
                    navigateUpOnEditAndOncePeopleAreAddedToNewTransaction()
                }
            }
        })

        binding.constraintlayoutAddedittransaction.setOnClickListener {
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view?.windowToken, 0)
        }

        binding.activityParticipants = viewModel.activity.participants!!
        binding.transaction = viewModel.transaction
        binding.cboAddedittransactionPaidby.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            viewModel.activity.participants!!.map { participant -> participant.toString() }
        )
        binding.btnAddedittransaction.text = getButtonName()
        binding.btnAddedittransaction.setOnClickListener {
            viewModel.createOrUpdate()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_addedittransactionoverflow, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.btn_addedittransactionoverflow_managepeople -> {
                val action = AddEditTransactionFragmentDirections
                    .actionAddEditTransactionFragmentToManagePeopleInTransactionFragment(viewModel.activity, viewModel.transaction.makeDTO())
                findNavController().navigate(action)
                return true
            }
            R.id.btn_addedittransactionoverflow_remove -> {
                viewModel.removeTransaction()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getButtonName() : String{
        return if (viewModel.isNewTransaction){
            getString(R.string.btn_add)
        } else {
            getString(R.string.btn_edit)
        }
    }

    private fun navigateUpOnEditAndOncePeopleAreAddedToNewTransaction(){
        findNavController().navigateUp()
    }

    private fun navigateAddPeopleOnNewTransaction(){
        val action = AddEditTransactionFragmentDirections
            .actionAddEditTransactionFragmentToManagePeopleInTransactionFragment(viewModel.activity, viewModel.transaction.makeDTO())
        viewModel.isNewTransaction = false
        findNavController().navigate(action)
    }
}