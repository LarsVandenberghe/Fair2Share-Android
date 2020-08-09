package com.example.fair2share.models.formdata_models

import com.example.fair2share.models.dto_models.ProfileDTOProperty

data class ProfileFormProperty(
    val profileId: Long,
    var firstname: String,
    var lastname: String,
    var email: String?,
    var friends: List<ProfileFormProperty>?,
    var friendRequestState: Int?,
    var activities: List<ActivityFormProperty>?,
    var amountOfFriendRequests: Int?
) {
    fun makeDTO(): ProfileDTOProperty {
        return ProfileDTOProperty(
            profileId,
            firstname,
            lastname,
            email,
            friends?.asDTO(),
            friendRequestState,
            activities?.asDTO(),
            amountOfFriendRequests
        )
    }

    override fun toString(): String {
        return String.format("%s %s", firstname, lastname)
    }
}


fun List<ProfileFormProperty>.asDTO(): List<ProfileDTOProperty> {
    return map {
        it.makeDTO()
    }
}