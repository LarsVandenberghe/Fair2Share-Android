package com.example.fair2share.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
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
            .inflate(R.layout.recycler_profile_activity, parent, false) as LinearLayout
        return RowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: RowItemViewHolder, position: Int) {
        val item = data[position]
        (holder.rowView.getChildAt(0) as TextView).text = String.format("%s", item.name)
    }
}