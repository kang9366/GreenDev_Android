package com.example.greendev.model

data class ImageResponse(
    val status: Int,
    val message: String,
    val data: ImageData
)

data class ImageData(
    val uploadFileName: String,
    val uploadFileUrl: String
)