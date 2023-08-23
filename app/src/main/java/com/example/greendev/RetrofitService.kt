package com.example.greendev

import com.example.greendev.model.AllCampaignResponse
import com.example.greendev.model.ApiResponse
import com.example.greendev.model.DetailCampaignResponse
import com.example.greendev.model.GrassResponse
import com.example.greendev.model.PostCampaign
import com.example.greendev.model.PostCampaignResponse
import com.example.greendev.model.PostResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface RetrofitService {
    @GET("api/v1/members/campaigns")
    fun getCampaigns(@Header("Authorization") token: String): Call<ApiResponse>

    @GET("api/v1/members/grass")
    fun getGrass(@Header("Authorization") token: String): Call<GrassResponse>

    @GET("api/v1/members/posts")
    fun getPosts(@Header("Authorization") token: String): Call<PostResponse>

    @GET("api/v1/campaigns?page=0&size=3&sort=joinCount,DESC")
    fun getAllCampaign(@Header("Authorization") token: String): Call<AllCampaignResponse>

    @GET("api/v1/campaigns/{campaignId}")
    fun getDetailCampaign(@Path("campaignId") campaignId: Int, @Header("Authorization") token: String): Call<DetailCampaignResponse>

    @Multipart
    @POST("api/v1/images")
    fun postImage(@Part imageFile: MultipartBody.Part, @Header("Authorization") token: String): Call<JsonObject>

    @POST("api/v1/campaigns")
    fun postCampaign(@Header("Authorization") token: String, @Body campaignData: PostCampaign): Call<PostCampaignResponse>
}