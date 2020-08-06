package com.example.fair2share.models.data_models

import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty

data class ActivityProperty (
    var activityId: Long?,
    var name: String,
    var description: String?,
    var currencyType: Int,
    var participants: List<ProfileProperty>?,
    var transactions: List<TransactionProperty>?
) {
    fun makeDTO(): ActivityDTOProperty {
        return ActivityDTOProperty(activityId, name, description, currencyType, participants?.asDTO(), transactions?.asDTO2())
    }
    companion object {
        fun makeEmpty() : ActivityProperty {
            return ActivityProperty(
                null,
                "",
                null,
                0,
                null,
                null
            )
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
) {
    fun makeDTO(): TransactionDTOProperty {
        return TransactionDTOProperty(transactionId, name, description, timeStamp, payment, profilesInTransaction?.asDTO(), paidBy!!.makeDTO())
    }

    companion object {
        fun makeEmpty() : TransactionProperty {
            return TransactionProperty(
                null,
                "",
                null,
                null,
                0.0,
                null,
                null
            )
        }
    }
}

fun List<ActivityProperty>.asDTO(): List<ActivityDTOProperty> {
    return map {
        it.makeDTO()
    }
}

fun List<TransactionProperty>.asDTO2(): List<TransactionDTOProperty> {
    return map {
        it.makeDTO()
    }
}