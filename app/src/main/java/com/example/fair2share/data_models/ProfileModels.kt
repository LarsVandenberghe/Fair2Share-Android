package com.example.fair2share.data_models

import android.os.Parcel
import android.os.Parcelable

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

    companion object CREATOR : Parcelable.Creator<ProfileProperty> {
        override fun createFromParcel(parcel: Parcel): ProfileProperty {
            return ProfileProperty(parcel)
        }

        override fun newArray(size: Int): Array<ProfileProperty?> {
            return arrayOfNulls(size)
        }
    }
}

data class ActivityProperty (
    val activityId: Long?,
    val name: String,
    var description: String?,
    val currencyType: Int,
    var participants: List<ProfileProperty>?,
    var transactions: List<TransactionProperty>?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.createTypedArrayList(ProfileProperty),
//        listOf<ProfileProperty>().apply {
//            parcel.readList(this, ProfileProperty::class.java.classLoader)
//        },
        null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(activityId!!)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(currencyType)
        parcel.writeTypedList(participants)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ActivityProperty> {
        override fun createFromParcel(parcel: Parcel): ActivityProperty {
            return ActivityProperty(parcel)
        }

        override fun newArray(size: Int): Array<ActivityProperty?> {
            return arrayOfNulls(size)
        }
    }
}

data class TransactionProperty(
    val transactionId: Long,
    val name: String,
    val description: String?,
    val timeStamp: String,
    val payment: Double,
    val profilesInTransaction: List<ProfileProperty>,
    val paidBy: ProfileProperty
)

//data class KeyValueProperty(
//    val key: Long,
//    val value: Any
//)