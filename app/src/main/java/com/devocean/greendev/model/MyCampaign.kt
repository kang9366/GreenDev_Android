package com.devocean.greendev.model

data class Campaign(
    val campaignId: Int,
    val title: String,
    val writer: String,
    val email: String,
    val description: String,
    val category: String?,
    val joinCount: Int,
    val joinMemberCount: Int,
    val date: String,
    val campaignImageUrl: String
)

data class ApiResponse(
    val status: Int,
    val message: String,
    val data: CampaignResponse
)

data class CampaignResponse(
    val campaigns: List<Campaign>,
    val count: Int
)