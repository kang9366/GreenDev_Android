package com.example.greendev

import com.example.greendev.model.AllCampaignResponse
import com.example.greendev.model.ApiResponse
import com.example.greendev.model.GrassResponse
import com.example.greendev.model.PostResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface RetrofitService {
    @GET("api/v1/members/campaigns")
    fun getCampaigns(@Header("Authorization") token: String): Call<ApiResponse>

    @GET("api/v1/members/grass")
    fun getGrass(@Header("Authorization") token: String): Call<GrassResponse>

    @GET("api/v1/members/posts")
    fun getPosts(@Header("Authorization") token: String): Call<PostResponse>

    @GET("api/v1/campaigns?page=0&size=3&sort=joinCount,DESC")
    fun getAllCampaign(@Header("Authorization") token: String): Call<AllCampaignResponse>
}