package com.devocean.greendev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devocean.greendev.R
import com.devocean.greendev.model.BadgeData

class BadgeAdapter(private val items: ArrayList<BadgeData>): RecyclerView.Adapter<BadgeAdapter.ViewHolder>(),
    ItemTouchHelperListener {
    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.badge_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.apply {
            index = position
            bind(item, position)
            itemView.tag = this
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
        private val badge: ImageView = view.findViewById(R.id.badgeImage)
        var index: Int = 0

        fun bind(item: BadgeData, position: Int) {
            index = position
            Glide.with(view)
                .load(item.imageUrl)
                .into(badge)
        }
    }

    override fun onItemMove(from: Int, to: Int) {
        TODO("Not yet implemented")
    }
}