package com.devocean.greendev.model

data class LoginBody(
    val email: String,
    val nickname: String,
    val username: String,
    val profileImageUrl: String
)