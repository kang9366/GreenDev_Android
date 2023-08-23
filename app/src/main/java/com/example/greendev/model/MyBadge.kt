package com.example.greendev.model

data class Badge(
    val badgeInstanceId: Int,
    val position: Int,
    val badgeImageUrl: String,
    val badgeName: String,
    val count: Int
)

data class BadgeResponse(
    val status: Int,
    val message: String,
    val data: MyBadgeData
)

data class MyBadgeData(
    val badges: List<Badge>,
    val count: Int
)