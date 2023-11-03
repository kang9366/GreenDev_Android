package com.devocean.greendev.model

data class DetailCampaign(
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

data class DetailCampaignResponse(
    val status: Int,
    val message: String,
    val data: DetailCampaign
)