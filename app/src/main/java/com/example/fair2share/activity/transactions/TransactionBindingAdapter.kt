package com.example.fair2share.activity.transactions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.R
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.data_models.Valutas
import com.example.fair2share.models.dto_models.TransactionDTOProperty

class TransactionBindingAdapter(val viewModel: ActivityTransactionsFragmentViewModel)  : RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data =  listOf<TransactionDTOProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_activitytransaction, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]

        holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_name).text = data[position].name
        holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_price).text = String.format("%s %.2f", Valutas.values()[viewModel.activityArg.currencyType].getSymbol(),data[position].payment)
        holder.rowView.setOnClickListener {
            val action = ActivityTransactionsFragmentDirections.actionActivityTransactionsFragmentToAddEditTransactionFragment(item, viewModel.activityArg)
            findNavController(holder.rowView).navigate(action)
        }
    }
}