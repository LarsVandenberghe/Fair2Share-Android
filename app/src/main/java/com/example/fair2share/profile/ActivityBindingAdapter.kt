package com.example.fair2share.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.R
import com.example.fair2share.models.dto_models.ActivityDTOProperty

class ActivityBindingAdapter(val viewModel: ProfileFragmentViewModel) :
    RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data = listOf<ActivityDTOProperty>()
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
        val action =
            ProfileFragmentDirections.actionFragmentProfileToActivityTransactionsFragment(item)

        holder.rowView.setOnClickListener {
            findNavController(holder.rowView).navigate(action)
        }

        holder.rowView.findViewById<TextView>(R.id.txt_recycleractivity_name).text =
            String.format("%s", item.name)
        holder.rowView.findViewById<ImageButton>(R.id.btn_recycleractivity_remove)
            .setOnClickListener {
                viewModel.removeActivity(holder.rowView.resources, item)
            }
    }
}