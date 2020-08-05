package com.example.fair2share.models.dto_models

import android.os.Parcel
import android.os.Parcelable
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.ProfileProperty
import com.example.fair2share.models.database_models.ProfileDatabaseProperty
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

data class ProfileDTOProperty (
    val profileId: Long,
    val firstname: String,
    val lastname: String,
    val email: String?,
    val friends: List<ProfileDTOProperty>?,
    val friendRequestState: Int?,
    val activities: List<ActivityDTOProperty>?,
    val amountOfFriendRequests: Int?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.createTypedArrayList(CREATOR),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(ActivityDTOProperty),
        parcel.readValue(Int::class.java.classLoader) as? Int
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(profileId)
        parcel.writeString(firstname)
        parcel.writeString(lastname)
        parcel.writeString(email)
        parcel.writeTypedList(friends)
        parcel.writeValue(friendRequestState)
        parcel.writeTypedList(activities)
        parcel.writeValue(amountOfFriendRequests)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun toString(): String {
        return String.format("%s %s", firstname, lastname)
    }

    fun makeDataModel(): ProfileProperty{
        return ProfileProperty(profileId, firstname, lastname, email, friends?.asDataModel(), friendRequestState, activities?.asDataModel(), amountOfFriendRequests)
    }

    fun makeDatabaseModel(): ProfileDatabaseProperty{
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(ProfileDTOProperty::class.java)
        return ProfileDatabaseProperty(
            profileId,
            jsonAdapter.toJson(this)
        )
    }

    companion object CREATOR : Parcelable.Creator<ProfileDTOProperty> {
        override fun createFromParcel(parcel: Parcel): ProfileDTOProperty {
            return ProfileDTOProperty(parcel)
        }

        override fun newArray(size: Int): Array<ProfileDTOProperty?> {
            return arrayOfNulls(size)
        }
    }
}

fun List<ProfileDTOProperty>.asDataModel(): List<ProfileProperty> {
    return map {
        it.makeDataModel()
    }
}