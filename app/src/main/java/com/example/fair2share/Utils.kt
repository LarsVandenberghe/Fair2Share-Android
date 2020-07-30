package com.example.fair2share

import android.widget.ImageView
import android.widget.TableRow
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.network.AccountApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.StringBuilder

class Utils {
    companion object {
        fun formExceptionsToString(exception: HttpException, customMessage: String? = null): String{
            val sb = StringBuilder()
            val charStream = exception.response().errorBody()?.charStream()
            if (charStream != null){
                var json = JSONObject(charStream.readText())
                try {
                    json = json.getJSONObject("errors")
                    json.keys().forEach {
                        val array = json.getJSONArray(it)
                        for (i in 0 until array.length()){
                            sb.append(array.getString(i))
                            sb.append("\n")
                        }
                    }
                } catch (e: JSONException){
                    if (customMessage != null){
                        sb.append(customMessage)
                    } else {
                        sb.append(exception.message())
                    }
                }
            }

            return sb.toString()
        }

        fun bindClientImageOnId(imgView: ImageView, imageId:Long?){
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
}