package com.example.fair2share.activity.transactions

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.database.Fair2ShareDatabase
import com.example.fair2share.databinding.FragmentAddedittransactionBinding
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty

class AddEditTransactionFragment : Fragment() {

    private lateinit var viewModel: AddEditTransactionViewModel
    private val safeArgs: AddEditTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        makeViewModel()
        setupObservables()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddedittransactionBinding>(inflater, R.layout.fragment_addedittransaction, container, false)

        setupUIObservables()
        bindViewModelData(binding)

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
                viewModel.removeTransaction(resources)
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

    private fun makeViewModel(){
        val database = Fair2ShareDatabase.getInstance(requireContext())
        val viewModelFactory = if (safeArgs.transaction != null){
            setHasOptionsMenu(true)
            AddEditTransactionViewModelFactory(safeArgs.activity, (safeArgs.transaction as TransactionDTOProperty).makeDataModel(), false, database)
        } else {
            AddEditTransactionViewModelFactory(safeArgs.activity, TransactionProperty.makeEmpty(), true, database)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTransactionViewModel::class.java)
    }

    private fun setupObservables(){
        viewModel.errorMessage.observe(this, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    private fun setupUIObservables(){
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it){
                if (viewModel.isNewTransaction){
                    navigateAddPeopleOnNewTransaction()
                } else {
                    navigateUpOnEditAndOncePeopleAreAddedToNewTransaction()
                }
            }
        })
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

    private fun bindViewModelData(binding: FragmentAddedittransactionBinding){
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
            viewModel.createOrUpdate(resources)
        }

    }
}