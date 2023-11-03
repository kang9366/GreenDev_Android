package com.devocean.greendev.model

data class AccessTokenResponse(
    val status: Int,
    val message: String,
    val data: AccessTokenData
)

data class AccessTokenData(
    val accessToken: String
)