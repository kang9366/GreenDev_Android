package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.adapter.RecordRecyclerViewAdapter
import com.example.greendev.adapter.CampaignRecyclerViewAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.databinding.FragmentHomeBinding
import com.example.greendev.model.CampaignData
import com.example.greendev.model.RecordData

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home, true) {
    private lateinit var campaignAdapter: CampaignRecyclerViewAdapter
    private lateinit var recordAdapter: RecordRecyclerViewAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initCampaignAdapter()
        initRecordAdapter()
        initItemTouchListener(campaignAdapter)
    }

    private fun initItemTouchListener(adapter: CampaignRecyclerViewAdapter){
        adapter.setOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("ResourceType")
            override fun onItemClick(v: View, data: CampaignData, pos: Int) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.apply {
                    replace(R.id.frameLayout, RecordFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        })
    }

    private fun initCampaignAdapter(){
        val campaignItem = ArrayList<CampaignData>()
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignItem.add(CampaignData("다다익선 캠페인", "스타벅스"))
        campaignAdapter = CampaignRecyclerViewAdapter(campaignItem, R.layout.main_campaign_item_layout)
        binding?.campaignRecyclerView?.adapter = campaignAdapter
    }

    private fun initRecordAdapter(){
        val recordItem = ArrayList<RecordData>()
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordItem.add(RecordData("2023-07-15", "다다익선 캠페인", "스타벅스"))
        recordAdapter = RecordRecyclerViewAdapter(recordItem)
        binding?.recordRecyclerView?.adapter = recordAdapter
    }
}