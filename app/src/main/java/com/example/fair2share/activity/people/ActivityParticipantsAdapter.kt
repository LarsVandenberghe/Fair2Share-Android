package com.example.fair2share.activity.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.models.dto_models.ProfileDTOProperty

class ActivityParticipantsAdapter(val viewModel: ManagePeopleInActivityViewModel): RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data =  listOf<ProfileDTOProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_addandremovefriend, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]
        val removeFriend = holder.rowView.getViewById(R.id.btn_recycleraddandremovefriend_removefriend) as ImageButton
        val addFriend = holder.rowView.getViewById(R.id.btn_recycleraddandremovefriend_addfriend) as ImageButton

        Utils.bindClientImageOnId((holder.rowView.getViewById(R.id.img_recycleraddandremovefriend_profile) as ImageView), item.profileId)
        (holder.rowView.getViewById(R.id.txt_recycleraddandremovefriend_name) as TextView).text = String.format("%s %s", item.firstname, item.lastname)

        addFriend.visibility = View.GONE

        removeFriend.setOnClickListener {
            viewModel.removeFromParticipants(item.profileId)
        }
    }
}