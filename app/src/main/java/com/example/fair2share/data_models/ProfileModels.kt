package com.example.fair2share.data_models

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.BindingAdapter

data class ProfileProperty (
    val profileId: Long,
    val firstname: String,
    val lastname: String,
    val email: String?,
    val friends: List<ProfileProperty>?,
    val friendRequestState: Int?,
    val activities: List<ActivityProperty>?,
    val amountOfFriendRequests: Int?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString(),
        parcel.createTypedArrayList(CREATOR),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.createTypedArrayList(ActivityProperty),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

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

    companion object CREATOR : Parcelable.Creator<ProfileProperty> {
        override fun createFromParcel(parcel: Parcel): ProfileProperty {
            return ProfileProperty(parcel)
        }

        override fun newArray(size: Int): Array<ProfileProperty?> {
            return arrayOfNulls(size)
        }
    }
}


