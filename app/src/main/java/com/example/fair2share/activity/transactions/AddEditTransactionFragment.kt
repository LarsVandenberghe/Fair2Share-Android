package com.example.fair2share.activity.transactions

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.NavArgs
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.fair2share.R
import com.example.fair2share.activity.ActivityFragmentViewModelFactory
import com.example.fair2share.data_models.ActivityProperty
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.data_models.TransactionProperty
import com.example.fair2share.data_models.Valutas
import com.example.fair2share.databinding.FragmentAddedittransactionBinding

class AddEditTransactionFragment : Fragment() {

    private lateinit var viewModel: AddEditTransactionViewModel
    private val safeArgs: AddEditTransactionFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelFactory = if (safeArgs.transaction != null){
            setHasOptionsMenu(true)
            AddEditTransactionViewModelFactory(safeArgs.activity, safeArgs.transaction as TransactionProperty, false)
        } else {
            AddEditTransactionViewModelFactory(safeArgs.activity, TransactionProperty.makeEmpty(), true)
        }

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddEditTransactionViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentAddedittransactionBinding>(inflater, R.layout.fragment_addedittransaction, container, false)

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

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        viewModel.navigate.observe(viewLifecycleOwner, Observer {
            if (it){
                if (viewModel.isNewTransaction){
                    val action = AddEditTransactionFragmentDirections
                        .actionAddEditTransactionFragmentToManagePeopleInTransactionFragment(viewModel.activity, viewModel.transaction)
                    viewModel.isNewTransaction = false
                    findNavController().navigate(action)
                } else {
                    findNavController().navigateUp()
                }
            }
        })

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
                    .actionAddEditTransactionFragmentToManagePeopleInTransactionFragment(viewModel.activity, viewModel.transaction)
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


}