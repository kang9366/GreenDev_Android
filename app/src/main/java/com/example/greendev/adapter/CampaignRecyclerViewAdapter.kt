package com.example.greendev.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greendev.R
import com.example.greendev.model.CampaignData

interface OnItemClickListener{
    fun onItemClick(v: View, data: CampaignData, pos : Int)
}

class CampaignRecyclerViewAdapter(private var items: ArrayList<CampaignData>, private val layout: Int): RecyclerView.Adapter<CampaignRecyclerViewAdapter.ViewHolder>() {
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
        val writer: TextView = view.findViewById(R.id.writer)
        val duration: TextView = view.findViewById(R.id.duration)
        val image: ImageView = view.findViewById(R.id.campaign_image)
        fun bind(item: CampaignData) {
            name.text = item.name
            writer.text = item.writer
            duration.text = item.duration
            Glide.with(view)
                .load(item.imageUrl)
                .into(image)
            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
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