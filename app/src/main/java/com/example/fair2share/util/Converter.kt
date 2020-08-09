package com.example.fair2share.util

import androidx.databinding.InverseMethod
import com.example.fair2share.models.dto_models.ProfileDTOProperty
import com.example.fair2share.models.formdata_models.ProfileFormProperty

object Converter {
    @InverseMethod("stringToDouble")
    @JvmStatic
    fun doubleToString(value: Double): String {
        return String.format("%.2f", value)
    }

    @JvmStatic
    fun stringToDouble(value: String): Double {
        return value.toDouble()
    }

    @InverseMethod("listIndexToFriend")
    @JvmStatic
    fun friendToListIndex(people: List<ProfileDTOProperty>, selected: ProfileFormProperty?): Int {
        return people.map { person -> person.profileId }.indexOf(selected?.profileId ?: 0)
    }

    @JvmStatic
    fun listIndexToFriend(people: List<ProfileDTOProperty>, selected: Int): ProfileFormProperty? {
        return people[selected].makeFormDataModel()
    }
}