package com.example.fair2share.models.dto_models

import android.os.Parcel
import android.os.Parcelable
import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.Valutas
import com.example.fair2share.models.database_models.ActivityDatabaseProperty
import com.example.fair2share.models.database_models.TransactionDatabaseProperty
import com.example.fair2share.models.formdata_models.ActivityFormProperty
import com.example.fair2share.models.formdata_models.TransactionFormProperty
import com.squareup.moshi.Moshi

data class ActivityDTOProperty(
    val activityId: Long?,
    val name: String,
    val description: String?,
    val currencyType: Int,
    val participants: List<ProfileDTOProperty>?,
    val transactions: List<TransactionDTOProperty>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readInt(),
        parcel.createTypedArrayList(ProfileDTOProperty),
        parcel.createTypedArrayList(TransactionDTOProperty)
    )

    init {
        checkValid()
    }

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

    fun makeFormDataModel(): ActivityFormProperty {
        return ActivityFormProperty(
            activityId,
            name,
            description,
            currencyType,
            participants?.asFormDataModel(),
            transactions?.asFormDataModel2()
        )
    }

    fun makeDatabaseModel(profileId: Long): ActivityDatabaseProperty {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(ActivityDTOProperty::class.java)
        return ActivityDatabaseProperty(
            profileId,
            activityId!!,
            jsonAdapter.toJson(this)
        )
    }


    private fun checkValid() {
        val exceptionsList = ArrayList<Int>()
        if (name.length < 3) {
            exceptionsList.add(R.string.name_not_valid)
        }

        if (!Valutas.values().map { it.ordinal }.contains(currencyType)) {
            exceptionsList.add(R.string.no_valuta_found)
        }

        if (exceptionsList.size > 0) {
            throw InvalidFormDataException(exceptionsList)
        }
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(ProfileDTOProperty),
        parcel.readParcelable(ProfileDTOProperty::class.java.classLoader)!!
    )

    init {
        checkValid()
    }

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

    fun makeFormDataModel(): TransactionFormProperty {
        return TransactionFormProperty(
            transactionId,
            name,
            description,
            timeStamp,
            payment,
            profilesInTransaction?.asFormDataModel(),
            paidBy.makeFormDataModel()
        )
    }

    fun makeDatabaseModel(profileId: Long, activityId: Long): TransactionDatabaseProperty {
        val moshi = Moshi.Builder().build()
        val jsonAdapter = moshi.adapter(TransactionDTOProperty::class.java)
        return TransactionDatabaseProperty(
            profileId,
            activityId,
            transactionId!!,
            jsonAdapter.toJson(this)
        )
    }

    private fun checkValid() {
        val exceptionsList = ArrayList<Int>()
        if (name.length < 3) {
            exceptionsList.add(R.string.name_not_valid)
        }

        if (payment < 0) {
            exceptionsList.add(R.string.transaction_payment_not_valid)
        }

        if (exceptionsList.size > 0) {
            throw InvalidFormDataException(exceptionsList)
        }
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

fun List<ActivityDTOProperty>.asFormDataModel(): List<ActivityFormProperty> {
    return map {
        it.makeFormDataModel()
    }
}

fun List<TransactionDTOProperty>.asFormDataModel2(): List<TransactionFormProperty> {
    return map {
        it.makeFormDataModel()
    }
}