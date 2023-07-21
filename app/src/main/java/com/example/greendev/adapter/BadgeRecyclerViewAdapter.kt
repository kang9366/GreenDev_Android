package com.example.greendev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.greendev.R
import com.example.greendev.model.BadgeData

class BadgeRecyclerViewAdapter(private val items: ArrayList<BadgeData>): RecyclerView.Adapter<BadgeRecyclerViewAdapter.ViewHolder>(),
    ItemTouchHelperListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.badge_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.badge.setImageDrawable(item.badge)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view: View = v
        val badge: ImageView = view.findViewById(R.id.badgeImage)
    }

    override fun onItemMove(from: Int, to: Int) {
        val item = items[from]
        items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }
}