package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.example.greendev.App
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.adapter.CampaignRecyclerViewAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.databinding.FragmentSearchBinding
import com.example.greendev.model.AllCampaignResponse
import com.example.greendev.model.CampaignData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search, true) {
    private lateinit var adapter: CampaignRecyclerViewAdapter
    private lateinit var campaignItem: ArrayList<CampaignData>
    private lateinit var searchItem: ArrayList<CampaignData>
    private val retrofitBuilder = RetrofitBuilder.retrofitService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        initItemFilter()
        initAllCampaign()
    }

    private fun initApplyFragment(){
        adapter.setOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("ResourceType")
            override fun onItemClick(v: View, data: CampaignData, pos: Int) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.apply {
                    replace(R.id.frameLayout, ApplyFragment(data.id))
                    addToBackStack(null)
                    commit()
                }
            }
        })
    }

    private fun initItemFilter(){
        searchItem = ArrayList()
        binding?.searchView?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val searchText: String = binding!!.searchView.text.toString()
                searchItem.clear()
                if(searchText == "") {
                    adapter.setItem(campaignItem)
                }else {
                    for(i in 0 until campaignItem.size) {
                        if (campaignItem[i].name.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.getDefault()))
                            || campaignItem[i].writer.lowercase(Locale.ROOT).contains(searchText.lowercase(Locale.getDefault()))) {
                            searchItem.add(campaignItem[i])
                        }
                        adapter.setItem(searchItem)
                    }
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun hideKeyboard() {
        binding?.layout?.setOnTouchListener {_, _ ->
            if (activity != null && requireActivity().currentFocus != null) {
                val inputManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    requireActivity().currentFocus!!.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
            false
        }
    }

    private fun initAllCampaign(){
        val getAllPosts: Call<AllCampaignResponse> = retrofitBuilder.getAllCampaign("Bearer ${App.preferences.token!!}")

        getAllPosts.enqueue(object : Callback<AllCampaignResponse> {
            override fun onResponse(call: Call<AllCampaignResponse>, response: Response<AllCampaignResponse>) {
                if(response.isSuccessful){
                    val data = response.body()!!.data
                    if(data.count==0){
                        binding?.emptyText?.visibility = View.VISIBLE
                    }else{
                        campaignItem = ArrayList()
                        for(i in 0 until data.count){
                            campaignItem.add(CampaignData(
                                data.campaigns[i].title,
                                data.campaigns[i].writer,
                                data.campaigns[i].campaignImageUrl,
                                data.campaigns[i].date,
                                data.campaigns[i].campaignId
                            ))
                        }
                        adapter = CampaignRecyclerViewAdapter(campaignItem, R.layout.campaign_item_layout)
                        binding?.campaignRecyclerView?.adapter = adapter
                        initApplyFragment()
                    }
                    Log.d("testtt", data.campaigns[0].toString())
                }else{
                    Log.d("testtt", "Fail : " + response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<AllCampaignResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }
}