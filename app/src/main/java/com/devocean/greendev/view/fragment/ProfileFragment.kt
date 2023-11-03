package com.devocean.greendev.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.devocean.greendev.App
import com.devocean.greendev.adapter.BadgeAdapter
import com.devocean.greendev.R
import com.devocean.greendev.adapter.ItemTouchCallback
import com.devocean.greendev.databinding.FragmentProfileBinding
import com.devocean.greendev.model.BadgeData
import com.devocean.greendev.model.BadgeResponse
import com.devocean.greendev.util.BindingFragment
import com.devocean.greendev.util.RetrofitBuilder
import com.devocean.greendev.view.dialog.LogoutDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile, true) {
    val item = ArrayList<BadgeData>()
    private val retrofitBuilder = RetrofitBuilder.api

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBadgeData()

        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(item))
        itemTouchHelper.attachToRecyclerView(binding?.badgeItemRecyclerView)
        binding?.profileTree?.setOnDragListener(DragListener())
        initLogout()
    }

    private fun initLogout() {
        binding?.logoutButton?.setOnClickListener {
            val dialog = LogoutDialog(context as AppCompatActivity)
            dialog.initDialog()

        }
    }

    private fun getBadgeData(){
        val getBadgeData: Call<BadgeResponse> = retrofitBuilder.getBadgeData("Bearer ${App.preferences.token!!}")
        getBadgeData.enqueue(object: Callback<BadgeResponse>{
            override fun onResponse(call: Call<BadgeResponse>, response: Response<BadgeResponse>) {
                if(response.isSuccessful){
                    val data = response.body()!!.data
                    if(data.count==0){
                        binding?.emptyText?.visibility = View.VISIBLE
                    }else{
                        for(i in 0 until data.count){
                            item.add(BadgeData(data.badges[i].badgeImageUrl))
                        }
                        val adapter = BadgeAdapter(item)
                        binding?.badgeItemRecyclerView?.adapter = adapter
                        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(item))
                        itemTouchHelper.attachToRecyclerView(binding?.badgeItemRecyclerView)
                    }
                }
            }

            override fun onFailure(call: Call<BadgeResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })

    }

    inner class DragListener : View.OnDragListener {
        override fun onDrag(v: View, event: DragEvent): Boolean {
            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    binding?.profileTree?.setBackgroundColor(Color.LTGRAY)
                    return true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    binding?.profileTree?.setBackgroundColor(Color.TRANSPARENT)
                    return true
                }
                DragEvent.ACTION_DROP -> {
                    val tvState: View = event.localState as View
                    val tvParent: ViewGroup = tvState.parent as ViewGroup
                    val container = v as FrameLayout

                    tvParent.removeView(tvState)
                    container.addView(tvState)

                    tvState.x = event.x -  tvState.width / 2
                    tvState.y = event.y - tvState.height / 2
                    v.visibility = View.VISIBLE
                    return true
                }
            }
            return false
        }
    }
}