package com.example.greendev.view.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.greendev.BindingFragment
import com.example.greendev.adapter.BadgeRecyclerViewAdapter
import com.example.greendev.R
import com.example.greendev.adapter.ItemTouchCallback
import com.example.greendev.databinding.FragmentProfileBinding
import com.example.greendev.model.BadgeData

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile, true) {
    val item = ArrayList<BadgeData>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        for(i in 0..20) item.add(BadgeData(requireActivity().getDrawable(R.drawable.badge_sample)!!))

        initRecyclerView(item)
        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(item))
        itemTouchHelper.attachToRecyclerView(binding?.badgeItemRecyclerView)
        binding?.profileTree?.setOnDragListener(DragListener())
    }

    private fun initRecyclerView(item: ArrayList<BadgeData>){
        val adapter = BadgeRecyclerViewAdapter(item)
        binding?.badgeItemRecyclerView?.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(item))
        itemTouchHelper.attachToRecyclerView(binding?.badgeItemRecyclerView)
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

                    tvState.x = event.x - tvState.width / 2
                    tvState.y = event.y - tvState.height / 2
                    v.visibility = View.VISIBLE
                    return true
                }
            }
            return false
        }
    }

}