package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.greendev.R
import com.example.greendev.adapter.RecordRecyclerViewAdapter
import com.example.greendev.adapter.CampaignRecyclerViewAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.databinding.FragmentMainBinding
import com.example.greendev.model.CampaignData
import com.example.greendev.model.RecordData
import com.example.greendev.view.dialog.FinishDialog

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var campaignAdapter: CampaignRecyclerViewAdapter
    private lateinit var recordAdapter: RecordRecyclerViewAdapter
    var onBackPressedCallback: OnBackPressedCallback? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMainBinding.bind(view)

        initCampaignAdapter()
        initRecordAdapter()
        initItemTouchListener(campaignAdapter)
        initBackpressedListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        onBackPressedCallback?.remove()
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

    private fun initBackpressedListener(){
        onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val dialog = FinishDialog(context as AppCompatActivity)
                dialog.initDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            onBackPressedCallback as OnBackPressedCallback
        )
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
        binding.campaignRecyclerView.adapter = campaignAdapter
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
        binding.recordRecyclerView.adapter = recordAdapter
    }
}