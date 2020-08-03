package com.example.fair2share.models.data_models

import android.os.Parcel
import android.os.Parcelable
import android.view.View
import androidx.databinding.BindingAdapter
import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.ProfileDTOProperty

data class ProfileProperty (
    val profileId: Long,
    val firstname: String,
    val lastname: String,
    val email: String?,
    val friends: List<ProfileProperty>?,
    val friendRequestState: Int?,
    val activities: List<ActivityProperty>?,
    val amountOfFriendRequests: Int?
) {
    fun makeDTO(): ProfileDTOProperty {
        return ProfileDTOProperty(profileId, firstname, lastname, email, friends?.asDTO(), friendRequestState, activities?.asDTO(), amountOfFriendRequests)
    }

    override fun toString(): String {
        return String.format("%s %s", firstname, lastname)
    }
}


fun List<ProfileProperty>.asDTO(): List<ProfileDTOProperty> {
    return map {
        it.makeDTO()
    }
}