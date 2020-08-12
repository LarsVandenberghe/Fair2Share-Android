package com.example.fair2share.utils

import android.widget.ImageView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.BuildConfig
import com.example.fair2share.R
import com.example.fair2share.network.AccountApi
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class Utils {
    companion object {
        fun formExceptionsToString(
            exception: HttpException,
            customMessage: String? = null
        ): String {
            val sb = StringBuilder()
            val text = exception.response().errorBody()?.charStream()?.readText()
            try {
                if (text != null) {
                    var json = JSONObject(text)
                    json = json.getJSONObject("errors")
                    json.keys().forEach {
                        val array = json.getJSONArray(it)
                        for (i in 0 until array.length()) {
                            sb.append(array.getString(i))
                            sb.append("\n")
                        }
                    }
                } else {
                    throw Exception()
                }
            } catch (e: Exception) {
                if (customMessage != null) {
                    sb.append(customMessage)
                } else {
                    sb.append(exception.message())
                }
            }

            return sb.toString()
        }

        fun throwExceptionIfHttpNotSuccessful(response: Response<out Any>) {
            if (!response.isSuccessful) {
                throw HttpException(response)
            }
        }

        fun bindClientImageOnId(imgView: ImageView, imageId: Long?) {
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


        fun getProfilePicUrl(imageId: Long): GlideUrl {
            val token = AccountApi.sharedPreferences
                .getString(Constants.SHARED_PREFERENCES_KEY_TOKEN, "") ?: ""
            return GlideUrl(
                String.format(
                    "%sProfile/image/%s",
                    BuildConfig.BASE_URL, imageId
                ),
                LazyHeaders.Builder()
                    .addHeader("Authorization", String.format("Bearer %s", token)).build()
            )
        }

        fun stopRefreshingAnimationAfter700ms(refreshLayout: SwipeRefreshLayout) {
            Timer(Constants.REFRESH_ANIMATION_TIMER_KEY, false).schedule(
                Constants.REFRESH_ANIMATION_TIMER
            ) {
                refreshLayout.isRefreshing = false
            }
        }
    }
}



