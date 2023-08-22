package com.example.greendev.model

data class AllCampaign(
    val campaignId: Int,
    val title: String,
    val writer: String,
    val description: String?,
    val category: String?,
    val joinCount: Int,
    val joinMemberCount: Int,
    val date: String,
    val campaignImageUrl: String
)

data class AllCampaignResponse(
    val status: Int,
    val message: String,
    val data: AllCampaignResponseData
)

data class AllCampaignResponseData(
    val campaigns: List<AllCampaign>,
    val count: Int,
    val totalPages: Int,
    val totalElements: Int
)