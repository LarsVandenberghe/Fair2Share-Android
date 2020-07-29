package com.example.fair2share.activity.summary

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.R
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.data_models.Valutas

class SummaryBindingAdapter(val viewModel: ActivitySummaryViewModel) : RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data =  listOf<Pair<ProfileProperty, Double>>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_activity_transaction, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_name).text = String.format("%s %s", data[position].first.firstname, data[position].first.lastname)
        holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_price).text = String.format("%s %.2f", Valutas.values()[viewModel.activity.currencyType].getSymbol(),data[position].second)
    }
}