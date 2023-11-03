package com.devocean.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.devocean.greendev.App
import com.devocean.greendev.R
import com.devocean.greendev.adapter.CampaignAdapter
import com.devocean.greendev.adapter.OnItemClickListener
import com.devocean.greendev.databinding.FragmentSearchBinding
import com.devocean.greendev.model.AllCampaignResponse
import com.devocean.greendev.model.AllCampaignResponseData
import com.devocean.greendev.model.CampaignData
import com.devocean.greendev.util.BindingFragment
import com.devocean.greendev.util.RetrofitBuilder
import com.devocean.greendev.view.dialog.FilterDialog
import com.devocean.greendev.view.dialog.InitDialogData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class SearchFragment : BindingFragment<FragmentSearchBinding>(R.layout.fragment_search, true) {
    private lateinit var adapter: CampaignAdapter
    private lateinit var campaignItem: ArrayList<CampaignData>
    private lateinit var searchItem: ArrayList<CampaignData>
    private lateinit var startDate: String
    private lateinit var endDate: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initItemFilter()
        initAllCampaign()
        initFilterDialog()
    }

    private fun initFilterDialog() {
        binding?.filter?.setOnClickListener {
            val dialog = FilterDialog(context as AppCompatActivity, this, object: InitDialogData{
                override fun initDialogData(data: String) {
                    val date = data.split(",")
                    startDate = date[0]
                    endDate = date[1]
                    initFilteredCampaign()
                }
            })

            dialog.setOnResetListener {
                Log.d("reset press", "reset!")
                initAllCampaign()
            }
            dialog.initDialog()
        }
    }

    private fun initFilteredCampaign() {
        RetrofitBuilder.api.getFilteredCampaign(startDate, endDate).enqueue(object: Callback<AllCampaignResponse>{
            override fun onResponse(call: Call<AllCampaignResponse>, response: Response<AllCampaignResponse>) {
                Log.d("testtt", response.body().toString())
                val data = response.body()!!.data
                Log.d("testtt", data.toString())
                if(data.count==0){
                    binding?.campaignRecyclerView?.visibility = View.GONE
                    binding?.emptyText?.visibility = View.VISIBLE
                }else{
                    binding?.campaignRecyclerView?.visibility = View.VISIBLE
                    binding?.emptyText?.visibility = View.GONE
                    initRecyclerView(data)
                    initApplyFragment()
                }
            }

            override fun onFailure(call: Call<AllCampaignResponse>, t: Throwable) {
                Log.d("testtt", t.message.toString())
            }
        })
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

    private fun initRecyclerView(data: AllCampaignResponseData) {
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
        adapter = CampaignAdapter(campaignItem, R.layout.campaign_item_layout)
        binding?.campaignRecyclerView?.adapter = adapter
    }

    private fun initAllCampaign(){
        val getAllPosts = RetrofitBuilder.api.getAllCampaign("Bearer ${App.preferences.token!!}")

        getAllPosts.enqueue(object : Callback<AllCampaignResponse> {
            override fun onResponse(call: Call<AllCampaignResponse>, response: Response<AllCampaignResponse>) {
                if(response.isSuccessful){
                    Log.d("tettt", response.body()!!.toString())
                    val data = response.body()!!.data
                    if(data.count==0){
                        binding?.campaignRecyclerView?.visibility = View.GONE
                        binding?.emptyText?.visibility = View.VISIBLE
                    }else{
                        binding?.campaignRecyclerView?.visibility = View.VISIBLE
                        binding?.emptyText?.visibility = View.GONE
                        initRecyclerView(data)
                        initApplyFragment()
                    }
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