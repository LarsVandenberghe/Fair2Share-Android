package com.example.fair2share.models.formdata_models

import com.example.fair2share.models.dto_models.ActivityDTOProperty
import com.example.fair2share.models.dto_models.TransactionDTOProperty

data class ActivityFormProperty(
    var activityId: Long?,
    var name: String,
    var description: String?,
    var currencyType: Int,
    var participants: List<ProfileFormProperty>?,
    var transactions: List<TransactionFormProperty>?
) {
    fun makeDTO(): ActivityDTOProperty {
        return ActivityDTOProperty(
            activityId,
            name,
            description,
            currencyType,
            participants?.asDTO(),
            transactions?.asDTO2()
        )
    }

    companion object {
        fun makeEmpty(): ActivityFormProperty {
            return ActivityFormProperty(
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

data class TransactionFormProperty(
    var transactionId: Long?,
    var name: String,
    var description: String?,
    val timeStamp: String?,
    var payment: Double,
    var profilesInTransaction: List<ProfileFormProperty>?,
    var paidBy: ProfileFormProperty?
) {
    fun makeDTO(): TransactionDTOProperty {
        return TransactionDTOProperty(
            transactionId,
            name,
            description,
            timeStamp,
            payment,
            profilesInTransaction?.asDTO(),
            paidBy!!.makeDTO()
        )
    }

    companion object {
        fun makeEmpty(): TransactionFormProperty {
            return TransactionFormProperty(
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

fun List<ActivityFormProperty>.asDTO(): List<ActivityDTOProperty> {
    return map {
        it.makeDTO()
    }
}

fun List<TransactionFormProperty>.asDTO2(): List<TransactionDTOProperty> {
    return map {
        it.makeDTO()
    }
}