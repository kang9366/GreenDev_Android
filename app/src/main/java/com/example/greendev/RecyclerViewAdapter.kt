package com.example.greendev

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter

data class CampaignData(val name: String, val company: String)
data class RecordData(val date: String, val campaign_title: String, val message: String)
data class BadgeData(val badge: Drawable)

class RecyclerViewAdapter(private var items: ArrayList<CampaignData>, private val layout: Int): Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view: View = v
        val name: TextView = view.findViewById(R.id.name)
        val company: TextView = view.findViewById(R.id.company)

        fun bind(item: CampaignData) {
            name.text = item.name
            company.text = item.company
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: CampaignData, pos : Int)
    }
    private var listener : OnItemClickListener? = null

    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setItem(filteredItem: ArrayList<CampaignData>){
        items = filteredItem
        notifyDataSetChanged()
    }
}

class RecordRecyclerViewAdapter(private val items: ArrayList<RecordData>): Adapter<RecordRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.record_item_layout, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.campaign_title.text = item.campaign_title
        holder.date.text = item.date
        holder.message.text = item.message
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var view: View = v
        val campaign_title: TextView = view.findViewById(R.id.campaign_title)
        val message: TextView = view.findViewById(R.id.message)
        val date: TextView = view.findViewById(R.id.date)
    }
}

interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int)
}

class BadgeRecyclerViewAdapter(private val items: ArrayList<BadgeData>): Adapter<BadgeRecyclerViewAdapter.ViewHolder>(), ItemTouchHelperListener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BadgeRecyclerViewAdapter.ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.badge_item, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: BadgeRecyclerViewAdapter.ViewHolder, position: Int) {
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

class ItemTouchCallback(private val listener: ItemTouchHelperListener) : ItemTouchHelper.Callback() {
    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = 0

        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        listener.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

    }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
}
