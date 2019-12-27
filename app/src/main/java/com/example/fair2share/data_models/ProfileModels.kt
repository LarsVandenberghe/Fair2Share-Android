package com.example.fair2share.data_models

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
)

data class TransactionProperty(
    val transactionId: Long,
    val name: String,
    val description: String?,
    val timeStamp: String,
    val payment: Double,
    val profilesInTransaction: List<ProfileProperty>,
    val paidBy: ProfileProperty
)