package com.devocean.greendev.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.devocean.greendev.model.BadgeData

interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int)
}

class ItemTouchCallback(val item: ArrayList<BadgeData>) : ItemTouchHelper.Callback() {
    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }
    override fun isLongPressDragEnabled(): Boolean = true

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
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        if (from < item.size && to < item.size) {
            item.removeAt(from)
            item.add(to, item[from])
            recyclerView.adapter?.notifyItemMoved(from, to)
        }
        return true
    }
}
