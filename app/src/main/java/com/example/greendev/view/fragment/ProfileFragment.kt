package com.example.greendev.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.greendev.BindingFragment
import com.example.greendev.adapter.BadgeRecyclerViewAdapter
import com.example.greendev.adapter.ItemTouchCallback
import com.example.greendev.R
import com.example.greendev.databinding.FragmentProfileBinding
import com.example.greendev.model.BadgeData

class ProfileFragment : BindingFragment<FragmentProfileBinding>(R.layout.fragment_profile, true) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = ArrayList<BadgeData>()

        for(i in 0..20) item.add(BadgeData(requireActivity().getDrawable(R.drawable.badge_sample)!!))

        initRecyclerView(item)
    }

    private fun initRecyclerView(item: ArrayList<BadgeData>){
        val adapter = BadgeRecyclerViewAdapter(item)
        binding?.badgeItemRecyclerView?.adapter = adapter

        val itemTouchHelper = ItemTouchHelper(ItemTouchCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding?.badgeItemRecyclerView)
    }
}