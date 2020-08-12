package com.example.fair2share.views.activity.summary

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.R
import com.example.fair2share.models.Valutas
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.utils.ConstraintRowItemViewHolder
import com.example.fair2share.viewmodels.activity.summary.ActivitySummaryViewModel

class SummaryBindingAdapter(val viewModel: ActivitySummaryViewModel) :
    RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data = listOf<Pair<ProfileDTOProperty, Double>>()
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
        holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_name).text =
            String.format("%s %s", data[position].first.firstname, data[position].first.lastname)
        val moneyField = holder.rowView.findViewById<TextView>(R.id.txt_recyclertransaction_price)
        moneyField.text = String.format(
            "%s %.2f",
            Valutas.values()[viewModel.activityArg.currencyType].getSymbol(),
            data[position].second
        )
        if (data[position].second < 0) {
            setTextColor(moneyField, holder, R.color.colorNegativeBalance)
        } else {
            setTextColor(moneyField, holder, R.color.colorPositiveBalance)
        }
    }

    private fun setTextColor(
        textView: TextView,
        holder: ConstraintRowItemViewHolder,
        resourceId: Int
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(holder.rowView.context.getColor(resourceId))
        } else {
            textView.setTextColor(holder.rowView.resources.getColor(resourceId))
        }
    }
}