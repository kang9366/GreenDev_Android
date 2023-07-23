package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.adapter.CampaignRecyclerViewAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.databinding.FragmentSearchBinding
import com.example.greendev.model.CampaignData
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search, true) {
    private lateinit var adapter: CampaignRecyclerViewAdapter
    private lateinit var item: ArrayList<CampaignData>
    private lateinit var searchItem: ArrayList<CampaignData>

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.searchView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("test", "before")
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("test", "on")
            }

            override fun afterTextChanged(p0: Editable?) {
                Log.d("test", "after")
                val searchText: String = binding!!.searchView.text.toString()
                searchItem.clear()
                if(searchText == "") {
                    adapter.setItem(item)
                }else {
                    for(i in 0 until item.size) {
                        if (item[i].name.toLowerCase().contains(searchText.lowercase(Locale.getDefault()))
                            || item[i].company.toLowerCase().contains(searchText.lowercase(Locale.getDefault()))) {
                            searchItem.add(item[i])
                        }
                        adapter.setItem(searchItem)
                    }
                }
            }
        })

        adapter.setOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("ResourceType")
            override fun onItemClick(v: View, data: CampaignData, pos: Int) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.apply {
                    replace(R.id.frameLayout, ApplyFragment())
                    addToBackStack(null)
                    commit()
                }
            }
        })

        binding?.layout?.setOnTouchListener { _, _ ->
            hideKeyboard()
            false
        }

        initRecyclerView()
    }

    private fun initRecyclerView(){
        //recycerview test
        item.add(CampaignData("데보션 캠페인", "SK"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        item.add(CampaignData("다다익선 캠페인", "스타벅스"))
        adapter = CampaignRecyclerViewAdapter(item, R.layout.campaign_item_layout)
        binding?.campaignRecyclerView?.adapter = adapter
    }

    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
}