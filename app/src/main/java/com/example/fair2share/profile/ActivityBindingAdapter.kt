package com.example.fair2share.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.R
import com.example.fair2share.RowItemViewHolder
import com.example.fair2share.data_models.ActivityProperty

class ActivityBindingAdapter : RecyclerView.Adapter<RowItemViewHolder>() {
    var data =  listOf<ActivityProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_profile_activity, parent, false) as ConstraintLayout
        return RowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RowItemViewHolder, position: Int) {
        val item = data[position]

        val bundle = Bundle()
        bundle.putParcelable("activity", item)
        holder.rowView.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_fragmentProfile_to_activityFragment, bundle))
        (holder.rowView.getChildAt(0) as TextView).text = String.format("%s", item.name)
    }
}