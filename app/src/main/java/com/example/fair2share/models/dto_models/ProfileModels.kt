package com.example.fair2share.models.dto_models

import android.os.Parcel
import android.os.Parcelable
import com.example.fair2share.R
import com.example.fair2share.exceptions.InvalidFormDataException
import com.example.fair2share.models.database_models.ProfileDatabaseProperty
import com.example.fair2share.models.formdata_models.ProfileFormProperty
import com.squareup.moshi.Moshi
import java.util.regex.Pattern

data class ProfileDTOProperty(
    val profileId: Long,
    val firstname: String,
    val lastname: String,
    val email: String?,
    val friends: List<ProfileDTOProperty>?,
    val friendRequestState: Int?,
    val activities: List<ActivityDTOProperty>?,
    val amountOfFriendRequests: Int?
) : Parcelable {
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

    init {
        checkValid()
    }

    private fun checkValid() {
        val exceptionsList = ArrayList<Int>()
        val spacesAndAllUnicodeChars = Pattern.compile("^[0-9\\p{L} .'-]+$")

        if (!spacesAndAllUnicodeChars.matcher(firstname).matches()) {
            exceptionsList.add(R.string.firtsname_not_valid)
        }

        if (!spacesAndAllUnicodeChars.matcher(lastname).matches()) {
            exceptionsList.add(R.string.lastname_not_valid)
        }

        if (email != null && !EMAIL_ADDRESS.matcher(email).matches()) {
            exceptionsList.add(R.string.email_not_valid)
        }

        if (exceptionsList.size > 0) {
            throw InvalidFormDataException(exceptionsList)
        }
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

    fun makeFormDataModel(): ProfileFormProperty {
        return ProfileFormProperty(
            profileId,
            firstname,
            lastname,
            email,
            friends?.asFormDataModel(),
            friendRequestState,
            activities?.asFormDataModel(),
            amountOfFriendRequests
        )
    }

    fun makeDatabaseModel(): ProfileDatabaseProperty {
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

fun List<ProfileDTOProperty>.asFormDataModel(): List<ProfileFormProperty> {
    return map {
        it.makeFormDataModel()
    }
}

// This regex is built into android.util.Patterns, but returns null on parameterized tests.
private val EMAIL_ADDRESS = Pattern.compile(
    "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")+"
)