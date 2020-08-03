package com.example.fair2share

import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.InverseMethod
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestOptions
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.network.AccountApi
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.StringBuilder

class Utils {
    companion object {
        fun formExceptionsToString(exception: HttpException, customMessage: String? = null): String{
            val sb = StringBuilder()
            val text = exception.response().errorBody()?.charStream()?.readText()
            try {
                if (text != null){
                    var json = JSONObject(text)
                    json = json.getJSONObject("errors")
                    json.keys().forEach {
                        val array = json.getJSONArray(it)
                        for (i in 0 until array.length()){
                            sb.append(array.getString(i))
                            sb.append("\n")
                        }
                    }
                } else {
                    if (customMessage != null){
                        sb.append(customMessage)
                    } else {
                        sb.append(exception.message())
                    }
                }
            } catch (e: JSONException){
                sb.append(text)
            }

            return sb.toString()
        }

        fun throwExceptionIfHttpNotSuccessful(response: Response<out Any>){
            if (!response.isSuccessful){
                throw HttpException(response)
            }
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

object Converter {
    @InverseMethod("stringToDouble")
    @JvmStatic fun doubleToString(value: Double): String {
        return String.format("%.2f", value)
    }

    @JvmStatic fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    @InverseMethod("listIndexToFriend")
    @JvmStatic fun friendToListIndex(people: List<ProfileDTOProperty>, selected: ProfileProperty?): Int {
        return people.map { person -> person.profileId }.indexOf(selected?.profileId ?: 0)
    }

    @JvmStatic fun listIndexToFriend(people: List<ProfileDTOProperty>, selected:Int): ProfileProperty? {
        return people[selected].makeDataModel()
    }
}