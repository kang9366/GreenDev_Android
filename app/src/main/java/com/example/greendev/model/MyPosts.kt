package com.example.greendev.model

data class Post(
    val postId: Int,
    val content: String,
    val postImageUrl: String,
    val date: String,
    val nickname: String,
    val email: String,
    val profileImageUrl: String
)

data class PostResponse(
    val status: Int,
    val message: String,
    val data: PostResponseData
)

data class PostResponseData(
    val posts: List<Post>,
    val count: Int
)