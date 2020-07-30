package com.example.fair2share.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.R
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.data_models.ActivityProperty

class ActivityBindingAdapter(val viewModel: ProfileFragmentViewModel) : RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data =  listOf<ActivityProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_profileactivity, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]

        val bundle = Bundle()
        bundle.putParcelable("activity", item)
        holder.rowView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_fragmentProfile_to_activityTransactionsFragment, bundle))
        holder.rowView.findViewById<TextView>(R.id.txt_recycleractivity_name).text = String.format("%s", item.name)
        holder.rowView.findViewById<ImageButton>(R.id.btn_recycleractivity_remove).setOnClickListener{
            viewModel.removeActivity(item)
        }
    }
}