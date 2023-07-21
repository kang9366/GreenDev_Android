package com.example.greendev.adapter

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

interface ItemTouchHelperListener {
    fun onItemMove(from: Int, to: Int)
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

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) { }

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }
}