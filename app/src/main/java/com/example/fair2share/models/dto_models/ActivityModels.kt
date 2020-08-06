package com.example.fair2share.models.dto_models

import android.os.Parcel
import android.os.Parcelable
import com.example.fair2share.models.data_models.ActivityProperty
import com.example.fair2share.models.data_models.TransactionProperty
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.TransactionDatabaseProperty
import com.squareup.moshi.Moshi

data class ActivityDTOProperty (
    val activityId: Long?,
    val name: String,
    val description: String?,
    val currencyType: Int,
    val participants: List<ProfileDTOProperty>?,
    val transactions: List<TransactionDTOProperty>?
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.createTypedArrayList(ProfileDTOProperty),
        parcel.createTypedArrayList(TransactionDTOProperty)
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

    fun makeDataModel(): ActivityProperty{
        return ActivityProperty(activityId, name, description, currencyType, participants?.asDataModel(), transactions?.asDataModel2())
    }

    fun makeDatabaseModel(profileId:Long): ActivityDatabaseProperty {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(ActivityDTOProperty::class.java)
        return ActivityDatabaseProperty(
            profileId,
            activityId!!,
            jsonAdapter.toJson(this)
        )
    }

    companion object CREATOR : Parcelable.Creator<ActivityDTOProperty> {
        override fun createFromParcel(parcel: Parcel): ActivityDTOProperty {
            return ActivityDTOProperty(parcel)
        }

        override fun newArray(size: Int): Array<ActivityDTOProperty?> {
            return arrayOfNulls(size)
        }
    }
}

data class TransactionDTOProperty(
    val transactionId: Long?,
    val name: String,
    val description: String?,
    val timeStamp: String?,
    val payment: Double,
    val profilesInTransaction: List<ProfileDTOProperty>?,
    val paidBy: ProfileDTOProperty
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(ProfileDTOProperty),
        parcel.readParcelable(ProfileDTOProperty::class.java.classLoader)!!
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

    fun makeDataModel(): TransactionProperty{
        return TransactionProperty(transactionId, name, description, timeStamp, payment, profilesInTransaction?.asDataModel(), paidBy.makeDataModel())
    }

    fun makeDatabaseModel(profileId:Long, activityId:Long): TransactionDatabaseProperty {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(TransactionDTOProperty::class.java)
        return TransactionDatabaseProperty(
            profileId,
            activityId,
            transactionId!!,
            jsonAdapter.toJson(this)
        )
    }

    companion object CREATOR : Parcelable.Creator<TransactionDTOProperty> {
        override fun createFromParcel(parcel: Parcel): TransactionDTOProperty {
            return TransactionDTOProperty(parcel)
        }

        override fun newArray(size: Int): Array<TransactionDTOProperty?> {
            return arrayOfNulls(size)
        }
    }
}

fun List<ActivityDTOProperty>.asDataModel(): List<ActivityProperty> {
    return map {
        it.makeDataModel()
    }
}

fun List<TransactionDTOProperty>.asDataModel2(): List<TransactionProperty> {
    return map {
        it.makeDataModel()
    }
}