package com.example.greendev

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class CampaignData(val name: String, val company: String)

class RecyclerViewAdapter(private val items: ArrayList<CampaignData>): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = LayoutInflater.from(parent.context).inflate(R.layout.campaign_item_layout, parent, false)
        return ViewHolder(inflatedView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.name.text = item.name
        holder.company.text = item.company
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v){
        private var view: View = v
        val name: TextView = view.findViewById(R.id.name)
        val company: TextView = view.findViewById(R.id.company)
    }
}