package com.devocean.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.devocean.greendev.App
import com.devocean.greendev.R
import com.devocean.greendev.databinding.FragmentApplyBinding
import com.devocean.greendev.model.DetailCampaignResponse
import com.devocean.greendev.util.BindingFragment
import com.devocean.greendev.util.RetrofitBuilder
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
        initApply()
    }

    private fun initApply() {
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        binding?.applyButton?.setOnClickListener {
            transaction?.apply {
                replace(R.id.frameLayout, CertificationFragment(campaignId!!))
                addToBackStack(null)
                commit()
            }
        }
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

                    binding?.title?.text = data.title
                    binding?.date?.text = "기간 : ${data.date}"
                    binding?.count?.text = "현재 ${data.joinMemberCount}명이 ${data.joinCount}번 참여중인 캠페인입니다."
                    binding?.detail?.text = "${data.description}"
                }
            }

            override fun onFailure(call: Call<DetailCampaignResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}