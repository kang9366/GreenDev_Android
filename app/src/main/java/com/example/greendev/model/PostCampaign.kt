package com.example.greendev.model

import com.google.gson.annotations.SerializedName

data class PostCampaignResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: PostCampaign
)

data class PostCampaign(
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String?,
    @SerializedName("joinCount") val joinCount: Int,
    @SerializedName("joinMemberCount") val joinMemberCount: Int,
    @SerializedName("date") val date: String,
    @SerializedName("campaignImageUrl") val campaignImageUrl: String
)