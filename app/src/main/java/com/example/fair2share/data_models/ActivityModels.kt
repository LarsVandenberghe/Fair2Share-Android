package com.example.fair2share.data_models

import android.os.Parcel
import android.os.Parcelable

data class ActivityProperty (
    var activityId: Long?,
    var name: String,
    var description: String?,
    var currencyType: Int,
    var participants: List<ProfileProperty>?,
    var transactions: List<TransactionProperty>?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.createTypedArrayList(ProfileProperty),
        parcel.createTypedArrayList(TransactionProperty)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(activityId!!)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(currencyType)
        parcel.writeTypedList(participants)
        parcel.writeTypedList(transactions)
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

        fun makeEmpty() : ActivityProperty{
            return ActivityProperty(null, "", null, 0, null, null)
        }
    }
}

data class TransactionProperty(
    var transactionId: Long?,
    var name: String,
    var description: String?,
    val timeStamp: String?,
    var payment: Double,
    var profilesInTransaction: List<ProfileProperty>?,
    var paidBy: ProfileProperty?
):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(ProfileProperty),
        parcel.readParcelable(ProfileProperty::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(transactionId!!)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(timeStamp)
        parcel.writeDouble(payment)
        parcel.writeTypedList(profilesInTransaction)
        parcel.writeParcelable(paidBy, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionProperty> {
        override fun createFromParcel(parcel: Parcel): TransactionProperty {
            return TransactionProperty(parcel)
        }

        override fun newArray(size: Int): Array<TransactionProperty?> {
            return arrayOfNulls(size)
        }

        fun makeEmpty() : TransactionProperty{
            return TransactionProperty(null, "", null, null, 0.0, null, null)
        }
    }
}