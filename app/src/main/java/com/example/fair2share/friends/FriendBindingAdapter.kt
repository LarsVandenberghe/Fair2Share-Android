package com.example.fair2share.friends

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.BuildConfig
import com.example.fair2share.ConstraintRowItemViewHolder
import com.example.fair2share.R
import com.example.fair2share.data_models.ProfileProperty

class FriendBindingAdapter() : RecyclerView.Adapter<ConstraintRowItemViewHolder>(){
    var data =  listOf<ProfileProperty>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConstraintRowItemViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onBindViewHolder(holder: ConstraintRowItemViewHolder, position: Int) {
        val item = data[position]

        bindClientImageOnId((holder.rowView.getChildAt(0) as ImageView), item.profileId)
        (holder.rowView.getChildAt(1) as TextView).text = String.format("%s%n%s", item.firstname, item.lastname)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    private fun bindClientImageOnId(imgView: ImageView, imageId:Long?){
        imageId?.let {
            val  imgUri = String.format("%sProfile/image/%d",  BuildConfig.BASE_URL, imageId).toUri()
            Glide.with(imgView.context)
                .load(imgUri)
                .apply(
                    RequestOptions().placeholder(R.drawable.default_user)
                        .error(R.drawable.default_user).diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                )
                .into(imgView)
        }
    }
}