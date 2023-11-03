package com.devocean.greendev.model

data class CampaignPostResponse(
    val status: Int,
    val message: String,
    val data: Data
)

data class Data(
    val posts: List<CampaignPost>,
    val count: Int,
    val totalPages: Int,
    val totalElements: Int
)

data class CampaignPost(
    val postId: Int,
    val content: String,
    val postImageUrl: String,
    val date: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String
)