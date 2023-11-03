package com.devocean.greendev.model

data class GrassCount(
    val date: String,
    val count: Int
)

data class GrassResponse(
    val status: Int,
    val message: String,
    val data: List<GrassCount>
)