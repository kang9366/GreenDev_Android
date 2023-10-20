package com.example.greendev.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.greendev.App.Companion.preferences
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.adapter.RecordAdapter
import com.example.greendev.adapter.MyCampaignAdapter
import com.example.greendev.adapter.OnItemClickListener
import com.example.greendev.adapter.SwipeToDeleteCallback
import com.example.greendev.databinding.FragmentHomeBinding
import com.example.greendev.model.AccessTokenResponse
import com.example.greendev.model.ApiResponse
import com.example.greendev.model.CampaignData
import com.example.greendev.model.GrassResponse
import com.example.greendev.model.PostResponse
import com.example.greendev.model.RecordData
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home, true) {
    private lateinit var myCampaignAdapter: MyCampaignAdapter
    private lateinit var postAdapter: RecordAdapter
    private val retrofitBuilder = RetrofitBuilder.api

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        GlobalScope.launch(Dispatchers.Main) {
            binding?.load?.visibility = View.VISIBLE
            binding?.view?.visibility = View.GONE

            delay(600)

            binding?.load?.visibility = View.GONE
            binding?.view?.visibility = View.VISIBLE
        }
        initMyCampaign()
        initMyPosts()
        getGrassData()
    }

    private fun initItemTouchListener(adapter: MyCampaignAdapter){
        adapter.setOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("ResourceType")
            override fun onItemClick(v: View, data: CampaignData, pos: Int) {
                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.apply {
                    replace(R.id.frameLayout, RecordFragment(data.id))
                    addToBackStack(null)
                    commit()
                }
            }
        })
    }

    private fun initMyCampaign(){
        val campaignItem = ArrayList<CampaignData>()
        val getCampaigns: Call<ApiResponse> = retrofitBuilder.getCampaigns("Bearer ${preferences.token!!}")
        val layout = binding?.layout
        val constraintSet = ConstraintSet()
        constraintSet.clone(layout)

        getCampaigns.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val data = response.body()!!.data
                    if(data.count==0){
                        binding?.emptyCampaign?.visibility = View.VISIBLE
                    }else{
                        for(i in 0 until data.count){
                            campaignItem.add(CampaignData(
                                data.campaigns[i].title,
                                data.campaigns[i].writer,
                                data.campaigns[i].campaignImageUrl,
                                data.campaigns[i].date,
                                data.campaigns[i].campaignId))
                        }
                        myCampaignAdapter = MyCampaignAdapter(campaignItem)
                        binding?.campaignRecyclerView?.adapter = myCampaignAdapter
                        initItemTouchListener(myCampaignAdapter)

                        constraintSet.connect(
                            binding?.recordText?.id ?: return, ConstraintSet.TOP,
                            binding?.campaignRecyclerView?.id ?: return, ConstraintSet.BOTTOM, convertDpToPixel(30f, requireContext())
                        )
                        constraintSet.applyTo(layout)
                    }
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }

    private fun reissueToken() {
        RetrofitBuilder.api.reissueToken().enqueue(object: Callback<AccessTokenResponse>{
            override fun onResponse(call: Call<AccessTokenResponse>, response: Response<AccessTokenResponse>) {
                if(response.isSuccessful) {
                    preferences.token = response.body()!!.data.accessToken
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    fun convertDpToPixel(dp: Float, context: Context): Int {
        return (dp * (context.resources
            .displayMetrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    private fun initMyPosts(){
        val postItem = ArrayList<RecordData>()
        val getPosts = retrofitBuilder.getMyPosts("Bearer ${preferences.token!!}")

        getPosts.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if(response.isSuccessful){
                    val data = response.body()!!.data

                    if(data.count==0){
                        Log.d("response data", "0000")
                        binding?.emptyPost?.visibility = View.VISIBLE
                    }else{
                        binding?.emptyPost?.visibility = View.GONE
                        for(i in 0 until response.body()?.data?.count!!){
                            postItem.add(RecordData(
                                response.body()!!.data.posts[i].date.split("T")[0],
                                response.body()!!.data.posts[i].campaignTitle,
                                response.body()!!.data.posts[i].content,
                                response.body()!!.data.posts[i].postId)
                            )
                        }
                        postAdapter = RecordAdapter(postItem)
                        val swipeHandler = SwipeToDeleteCallback(postAdapter, context as AppCompatActivity, binding?.recordRecyclerView!!)
                        val itemTouchHelper = ItemTouchHelper(swipeHandler)
                        itemTouchHelper.attachToRecyclerView(binding?.recordRecyclerView)
                        binding?.recordRecyclerView?.adapter = postAdapter
                    }
                }else{
                    reissueToken()
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.d("NetWorkError", "${t.message}")
            }
        })
    }

    @SuppressLint("ResourceAsColor")
    private fun initGrassView(text: String, count: Int) {
        val gridLayout = binding?.gridRecord
        val grass = LayoutInflater.from(requireContext()).inflate(R.layout.grass_layout, gridLayout, false) as TextView
        val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.grid_record_item)
        if(count==0) {
            grass.apply {
                drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.gray), PorterDuff.Mode.SRC_IN)
                background = drawable
            }
        }else {
            grass.apply {
                drawable?.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.button_color), PorterDuff.Mode.SRC_IN)
                background = drawable
            }
        }
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
            getGrass.enqueue(object : Callback<GrassResponse> {
                override fun onResponse(call: Call<GrassResponse>, response: Response<GrassResponse>) {
                    if(response.isSuccessful){
                        for(i in response.body()!!.data.reversed()){
                            initGrassView(i.date + ", " + i.count + "회 참여", i.count)
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
}