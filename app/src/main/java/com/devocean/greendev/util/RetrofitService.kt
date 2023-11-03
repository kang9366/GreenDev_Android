package com.devocean.greendev.util

import com.devocean.greendev.App.Companion.preferences
import com.devocean.greendev.model.AccessTokenResponse
import com.devocean.greendev.model.AllCampaignResponse
import com.devocean.greendev.model.ApiResponse
import com.devocean.greendev.model.BadgeResponse
import com.devocean.greendev.model.CampaignPostResponse
import com.devocean.greendev.model.CertificationBody
import com.devocean.greendev.model.DetailCampaignResponse
import com.devocean.greendev.model.GrassResponse
import com.devocean.greendev.model.ImageResponse
import com.devocean.greendev.model.LoginBody
import com.devocean.greendev.model.PostCampaign
import com.devocean.greendev.model.PostCampaignResponse
import com.devocean.greendev.model.PostResponse
import com.devocean.greendev.model.RefreshTokenResponse
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitService {
    @GET("api/v1/members/campaigns")
    fun getCampaigns(@Header("Authorization") token: String): Call<ApiResponse>

    @GET("api/v1/members/grass")
    fun getGrass(@Header("Authorization") token: String): Call<GrassResponse>

    @GET("api/v1/members/posts")
    fun getMyPosts(@Header("Authorization") token: String): Call<PostResponse>

    @GET("api/v1/campaigns?page=0&size=10&sort=joinCount,DESC")
    fun getAllCampaign(@Header("Authorization") token: String): Call<AllCampaignResponse>

    @GET("api/v1/campaigns/{campaignId}")
    fun getDetailCampaign(@Path("campaignId") campaignId: Int, @Header("Authorization") token: String): Call<DetailCampaignResponse>

    @POST("api/v1/app/token/login/naver")
    fun naverLogin(
        @Body data: LoginBody
    ): Call<AccessTokenResponse>

    @Multipart
    @POST("api/v1/images")
    fun postImage(
        @Part file: MultipartBody.Part
    ): Call<ImageResponse>

    @POST("api/v1/campaigns")
    fun postCampaign(@Header("Authorization") token: String, @Body campaignData: PostCampaign): Call<PostCampaignResponse>

    @GET("api/v1/profile")
    fun getBadgeData(@Header("Authorization") token: String): Call<BadgeResponse>

    @POST("api/v1/app/token/refreshToken")
    fun getRefreshToken(@Body accessToken: String = preferences.token!!): Call<RefreshTokenResponse>

    @POST("api/v1/app/token/refresh")
    fun reissueToken(@Body refreshToken: String = preferences.refreshToken!!): Call<AccessTokenResponse>

    @POST("api/v1/app/token/blacklist")
    fun logout(@Body refreshToken: String = preferences.refreshToken!!): Call<JsonObject>

    @GET("api/v1/campaigns/{campaignId}/posts?page=0&size=1000&sort=DESC")
    fun getCampaignPost(@Path("campaignId") campaignId: Int, @Header("Authorization") token: String): Call<CampaignPostResponse>

    @DELETE("api/v1/posts/{postId}")
    fun deletePost(@Path("postId") postId: Int, @Header("Authorization") token: String): Call<JsonObject>

    @POST("api/v1/campaigns/{campaignId}/posts")
    fun postCertification(
        @Path("campaignId") campaignId: Int,
        @Header("Authorization") token: String,
        @Body certifiationData: CertificationBody
    ): Call<JsonObject>

    @GET("api/v1/campaigns/dates?")
    fun getFilteredCampaign(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sort") sort: String = "joinCount,DESC",
        @Header("Authorization") token: String = "Bearer ${preferences.token}"
    ): Call<AllCampaignResponse>
}