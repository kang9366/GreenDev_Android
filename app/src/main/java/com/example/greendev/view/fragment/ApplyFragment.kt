package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.example.greendev.App
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.databinding.FragmentApplyBinding
import com.example.greendev.model.DetailCampaignResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApplyFragment(campaignId: Int) : BindingFragment<FragmentApplyBinding>(R.layout.fragment_apply, false) {
    private var campaignId: Int? = null
    private val retrofitBuilder = RetrofitBuilder.api

    init {
        this.campaignId = campaignId
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }
        initCampaignDetail()
    }

    private fun initCampaignDetail(){
        val getDetailCampaign: Call<DetailCampaignResponse> = retrofitBuilder.getDetailCampaign(campaignId!!,"Bearer ${App.preferences.token!!}")
        getDetailCampaign.enqueue(object : Callback<DetailCampaignResponse>{
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<DetailCampaignResponse>, response: Response<DetailCampaignResponse>) {
                if(response.isSuccessful){
                    val data = response.body()!!.data
                    Glide.with(requireContext())
                        .load(data.campaignImageUrl)
                        .error(R.drawable.sample)
                        .into(binding?.campaignImage!!)

                    binding?.titleText?.text = data.title
                    binding?.title?.text = "캠페인 명 : ${data.title}"
                    binding?.date?.text = "기간 : ${data.date}"
                    binding?.count?.text = "현재 ${data.joinMemberCount}명이 ${data.joinCount}번 참여중인 캠페인입니다."
                }
            }

            override fun onFailure(call: Call<DetailCampaignResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}