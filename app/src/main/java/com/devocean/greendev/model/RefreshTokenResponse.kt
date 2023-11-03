package com.devocean.greendev.model

data class RefreshTokenResponse(
    val status: Int,
    val message: String,
    val data: RefreshTokenData
)

data class RefreshTokenData(
    val refreshToken: String,
    val refreshTokenRemainingTime: Long
)
