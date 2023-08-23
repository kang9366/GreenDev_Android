package com.example.greendev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
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
        holder.apply {
            bind(item)
            itemView.setOnLongClickListener {
                val clipData = View.DragShadowBuilder(it)
                it.startDragAndDrop(null, clipData, it, 0)
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view: View = v
        val badge: ImageView = view.findViewById(R.id.badgeImage)
        fun bind(item: BadgeData) {
            Glide.with(view)
                .load(item.imageUrl)
                .into(badge)
        }
    }

    override fun onItemMove(from: Int, to: Int) {
        val item = items[from]
        items.removeAt(from)
        items.add(to, item)
        notifyItemMoved(from, to)
    }
}