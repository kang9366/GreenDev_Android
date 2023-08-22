package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.marginBottom
import com.example.greendev.App.Companion.preferences
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.adapter.RecordRecyclerViewAdapter
import com.example.greendev.adapter.CampaignRecyclerViewAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.databinding.FragmentHomeBinding
import com.example.greendev.model.ApiResponse
import com.example.greendev.model.CampaignData
import com.example.greendev.model.GrassResponse
import com.example.greendev.model.PostResponse
import com.example.greendev.model.RecordData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home, true) {
    private lateinit var campaignAdapter: CampaignRecyclerViewAdapter
    private lateinit var postAdapter: RecordRecyclerViewAdapter
    private val retrofitBuilder = RetrofitBuilder.retrofitService

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMyCampaign()
        initMyPosts()
        initGrass()
        Log.d("testtt", "Token : " + preferences.token.toString())
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

    private fun initMyCampaign(){
        val campaignItem = ArrayList<CampaignData>()
        val getCampaigns: Call<ApiResponse> = retrofitBuilder.getCampaigns("Bearer ${preferences.token!!}")

        getCampaigns.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    Log.d("testtt", data.count.toString())
                    if(data.count==0){
                        binding?.emptyCampaign?.visibility = View.VISIBLE
                    }else{
                        for(i in 0 until data.count){
                            campaignItem.add(CampaignData(
                                data.campaigns[i].title,
                                data.campaigns[i].description))
                        }
                        campaignAdapter = CampaignRecyclerViewAdapter(campaignItem, R.layout.main_campaign_item_layout)
                        binding?.campaignRecyclerView?.adapter = campaignAdapter
                        initItemTouchListener(campaignAdapter)
                    }
                } else {
                    Log.d("testtt", "Fail : " + response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }

    private fun initMyPosts(){
        val postItem = ArrayList<RecordData>()
        val getPosts: Call<PostResponse> = retrofitBuilder.getPosts("Bearer ${preferences.token!!}")

        getPosts.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    val data = response.body()!!.data
                    if(data.count==0){
                        binding?.emptyPost?.visibility = View.VISIBLE
                    }else{
                        for(i in 0 until response.body()?.data?.count!!){
                            postItem.add(RecordData(
                                response.body()!!.data.posts[i].date,
                                response.body()!!.data.posts[i].content,
                                response.body()!!.data.posts[i].nickname))
                        }
                        postAdapter = RecordRecyclerViewAdapter(postItem)
                        binding?.recordRecyclerView?.adapter = postAdapter
                    }
                }else{
                    Log.d("testtt", "Fail : " + response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }

    private fun initGrass(){
        val getGrass: Call<GrassResponse> = retrofitBuilder.getGrass("Bearer ${preferences.token!!}")

        getGrass.enqueue(object : Callback<GrassResponse> {
            override fun onResponse(call: Call<GrassResponse>, response: Response<GrassResponse>) {
                if(response.isSuccessful){
//                   Log.d("testtt", "date : " + response.body()!!.data)
                }else{
                    Log.d("testtt", "Fail Grass : " + response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<GrassResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }
}