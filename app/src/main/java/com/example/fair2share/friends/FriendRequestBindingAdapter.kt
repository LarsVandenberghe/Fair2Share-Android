package com.example.fair2share.friends

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
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
import com.example.fair2share.data_models.ProfileProperty
import com.example.fair2share.network.AccountApi

class FriendRequestBindingAdapter(val viewModel: FriendListViewModel) : RecyclerView.Adapter<ConstraintRowItemViewHolder>(){
    var data =  listOf<ProfileProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.recycler_friend_request_profile, parent, false) as ConstraintLayout
        return ConstraintRowItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]
        bindClientImageOnId((holder.rowView.getChildAt(0) as ImageView), item.profileId)
        (holder.rowView.getChildAt(1) as TextView).text = String.format("%s %s", item.firstname, item.lastname)
        (holder.rowView.getViewById(R.id.btn_recyclerfriendrequest_addfriend) as ImageButton).setOnClickListener {
            viewModel.handleFriendRequest(item.profileId, true)
        }
        (holder.rowView.getViewById(R.id.btn_recyclerfriendrequest_removefriend) as ImageButton).setOnClickListener {
            viewModel.handleFriendRequest(item.profileId, false)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun bindClientImageOnId(imgView: ImageView, imageId:Long?){
        imageId?.let {
            Glide.with(imgView.context)
                .load(getProfilePicUrl(it))
                .apply(
                    RequestOptions().placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(imgView)
        }
    }


    fun getProfilePicUrl(imageId:Long): GlideUrl {
        val token = AccountApi.sharedPreferences?.getString("token", "") ?: ""
        return GlideUrl(String.format("%sProfile/image/%s", BuildConfig.BASE_URL, imageId), LazyHeaders.Builder()
            .addHeader("Authorization", String.format("Bearer %s", token)).build())
    }
}