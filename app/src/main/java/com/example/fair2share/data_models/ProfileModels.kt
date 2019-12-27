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
)

data class ActivityProperty (
    val activityId: Long,
    val name: String,
    val description: String?,
    val currencyType: Int,
    val participants: List<ProfileProperty>?,
    val transactions: List<TransactionProperty>?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        null,
        null
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(activityId)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(currencyType)
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