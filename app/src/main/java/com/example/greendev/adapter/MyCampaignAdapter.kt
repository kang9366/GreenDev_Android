package com.example.greendev.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.greendev.R
import com.example.greendev.model.CampaignData

class MyCampaignAdapter(private var items: ArrayList<CampaignData>): RecyclerView.Adapter<MyCampaignAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.my_campaign_item_layout, parent, false)
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
        val image: ImageView = view.findViewById(R.id.campaign_image)
        var id: Int = 0
        fun bind(item: CampaignData) {
            name.text = item.name
            writer.text = item.writer
            id = item.id
            Glide.with(view)
                .load(item.imageUrl)
                .error(R.drawable.test)
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
}