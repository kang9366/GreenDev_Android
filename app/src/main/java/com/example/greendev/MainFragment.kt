package com.example.greendev

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.greendev.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMainBinding.bind(view)
        val item = ArrayList<CampaignData>()

        //recycerview test
        item.add(CampaignData("다다익선 캠페인","스타벅스"))
        item.add(CampaignData("다다익선 캠페인","스타벅스"))
        item.add(CampaignData("다다익선 캠페인","스타벅스"))
        item.add(CampaignData("다다익선 캠페인","스타벅스"))
        item.add(CampaignData("다다익선 캠페인","스타벅스"))
        item.add(CampaignData("다다익선 캠페인","스타벅스"))

        val adapter = RecyclerViewAdapter(item, R.layout.main_campaign_item_layout)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }
}