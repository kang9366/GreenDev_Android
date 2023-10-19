package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
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
import com.google.gson.JsonObject
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home, true) {
    private lateinit var campaignAdapter: CampaignRecyclerViewAdapter
    private lateinit var postAdapter: RecordRecyclerViewAdapter
    private val retrofitBuilder = RetrofitBuilder.api

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initMyCampaign()
        initMyPosts()
        getGrassData()
        getRefreshToken()
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
                                data.campaigns[i].description,
                                data.campaigns[i].campaignImageUrl,
                                data.campaigns[i].date,
                                data.campaigns[i].campaignId))
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

    private fun initGrassView(text: String) {
        val gridLayout = binding?.gridRecord
        val grass = LayoutInflater.from(requireContext()).inflate(R.layout.grass_layout, gridLayout, false) as TextView
        val balloon = initTooltip(text)
        gridLayout?.addView(grass)
        grass.setOnClickListener {
            balloon.showAlignTop(it)
        }
    }

    private fun initTooltip(text: String): Balloon {
        val balloon = createBalloon(requireContext()) {
            setWidth(BalloonSizeSpec.WRAP)
            setHeight(BalloonSizeSpec.WRAP)
            setText(text)
            setTextColorResource(R.color.white)
            setTextSize(15f)
            setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            setArrowSize(10)
            setArrowPosition(0.5f)
            setPadding(12)
            setCornerRadius(8f)
            setBackgroundColorResource(R.color.black)
            setBalloonAnimation(BalloonAnimation.ELASTIC)
            setLifecycleOwner(lifecycleOwner)
            build()
        }
        return balloon
    }

    private fun getGrassData(){
        val getGrass: Call<GrassResponse> = retrofitBuilder.getGrass("Bearer ${preferences.token!!}")
        CoroutineScope(Dispatchers.Main).launch {
            binding?.load?.playAnimation()
            getGrass.enqueue(object : Callback<GrassResponse> {
                override fun onResponse(call: Call<GrassResponse>, response: Response<GrassResponse>) {
                    if(response.isSuccessful){
                        for(i in response.body()!!.data.reversed()){
                            initGrassView(i.date + ", " + i.count + "회 참여")
                        }
                        binding?.load?.apply {
                            visibility = View.GONE
                            cancelAnimation()
                        }
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

    private fun getRefreshToken(){
        RetrofitBuilder.api.getRefreshToken("refreshToken=${preferences.token!!}").enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Log.d("my token", preferences.token!!)
                Log.d("get refresh token", response.toString())
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("get refresh token error", t.message.toString())
            }
        })
    }
}