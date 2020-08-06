package com.example.fair2share

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
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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
            val token = AccountApi.sharedPreferences.getString("token", "") ?: ""
            return GlideUrl(
                String.format("%sProfile/image/%s", BuildConfig.BASE_URL, imageId),
                LazyHeaders.Builder()
                    .addHeader("Authorization", String.format("Bearer %s", token)).build()
            )
        }
    }
}

object Converter {
    @InverseMethod("stringToDouble")
    @JvmStatic
    fun doubleToString(value: Double): String {
        return String.format("%.2f", value)
    }

    @JvmStatic
    fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    @InverseMethod("listIndexToFriend")
    @JvmStatic
    fun friendToListIndex(people: List<ProfileDTOProperty>, selected: ProfileProperty?): Int {
        return people.map { person -> person.profileId }.indexOf(selected?.profileId ?: 0)
    }

    @JvmStatic
    fun listIndexToFriend(people: List<ProfileDTOProperty>, selected: Int): ProfileProperty? {
        return people[selected].makeDataModel()
    }
}

// The issue:
// https://github.com/square/moshi/issues/508
// The solution
// https://github.com/loewenfels/dep-graph-releaser/blob/66c822830aa38ac6b4a2278dfe0020d551782bf0/dep-graph-releaser-serialization/src/main/kotlin/ch/loewenfels/depgraph/serialization/PairAdapterFactory.kt
object PairAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        if (type !is ParameterizedType) {
            return null
        }
        if (Pair::class.java != type.rawType) {
            return null
        }
        val profileType = type.actualTypeArguments[0]
        if (ProfileDTOProperty::class.java != profileType) {
            return null
        }
        val doubleType = type.actualTypeArguments[1]

        if (Double::class.javaObjectType != doubleType) {
            return null
        }

        return PairAdapter(
            moshi.adapter(type.actualTypeArguments[0]),
            moshi.adapter(type.actualTypeArguments[1])
        )
    }

    private class PairAdapter(
        private val firstAdapter: JsonAdapter<ProfileDTOProperty>,
        private val secondAdapter: JsonAdapter<Double>
    ) : JsonAdapter<Pair<Any, Any>>() {

        companion object {
            val NAMES = JsonReader.Options.of(
                "profileId",
                "firstname",
                "lastname",
                "email",
                "friendRequestState"
            )
        }

        override fun toJson(writer: JsonWriter, value: Pair<Any, Any>?) {
            writer.beginArray()
            firstAdapter.toJson(writer, value!!.first as ProfileDTOProperty)
            secondAdapter.toJson(writer, value.second as Double)
            writer.endArray()
        }

        override fun fromJson(reader: JsonReader): Pair<Any, Any>? {
            reader.beginArray()
            reader.beginObject()
            var profileId: Long = -1L
            var firstname = ""
            var lastname = ""
            var email: String? = null
            var friendRequestState = 0
            while (reader.hasNext()) {
                when (reader.selectName(NAMES)) {
                    0 -> profileId = reader.nextLong()
                    1 -> firstname = reader.nextString()
                    2 -> lastname = reader.nextString()
                    3 -> email = reader.nextString()
                    4 -> friendRequestState = reader.nextInt()
                    else -> {
                        reader.skipName()
                        reader.skipValue()
                    }
                }
            }
            reader.endObject()
            val first = ProfileDTOProperty(
                profileId,
                firstname,
                lastname,
                email,
                null,
                null,
                null,
                friendRequestState
            )

            val second = reader.nextDouble()
            reader.endArray()
            return first to second
        }
    }
}