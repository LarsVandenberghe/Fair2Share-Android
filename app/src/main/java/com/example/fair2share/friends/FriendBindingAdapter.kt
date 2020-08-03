package com.example.fair2share.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.BuildConfig
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.R
import com.example.fair2share.Utils
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi

class FriendBindingAdapter: RecyclerView.Adapter<ConstraintRowItemViewHolder>() {
    var data =  listOf<ProfileDTOProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_friendprofile, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]
        Utils.bindClientImageOnId((holder.rowView.getViewById(R.id.img_recyclerfriend_profile) as ImageView), item.profileId)
        (holder.rowView.getViewById(R.id.txt_recyclerfriend_name) as TextView).text = String.format("%s %s", item.firstname, item.lastname)
    }
}