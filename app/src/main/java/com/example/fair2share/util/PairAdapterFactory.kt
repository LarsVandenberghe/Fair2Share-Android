package com.example.fair2share.util

import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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