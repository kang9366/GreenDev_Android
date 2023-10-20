package com.example.greendev.adapter

import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.greendev.App
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.view.dialog.DeleteDialog
import com.example.greendev.view.dialog.DeleteDialogListener
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.max

class SwipeToDeleteCallback(
    private val adapter: RecordAdapter,
    private val context: AppCompatActivity,
    recyclerView: RecyclerView
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
    private var itemTouchHelper: ItemTouchHelper

    init {
        val swipeHandler = this
        itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        val postId = adapter.getItem(position)
        val dialog = DeleteDialog(context, object: DeleteDialogListener {
            override fun onDeleteClicked() {
                adapter.removeItem(position)
                initDelete(postId)
            }

            override fun onCancelClicked() {
                val animator = ObjectAnimator.ofFloat(viewHolder.itemView, "translationX", 0f)
                animator.duration = 250
                animator.start()
                itemTouchHelper.startSwipe(viewHolder)
            }
        })
        dialog.initDialog()
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        viewHolder.itemView.translationX = 0f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder.itemView
        val maxDx = max(-itemView.width.toFloat() / 4, dX)

        c.clipRect(itemView.left, itemView.top, itemView.right, itemView.bottom)

        val paint = Paint()
        paint.color = ContextCompat.getColor(recyclerView.context, R.color.red)
        c.drawRect(itemView.right + maxDx, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat(), paint)

        val textPaint = Paint()
        val font = ResourcesCompat.getFont(context, R.font.gmarketsansttfmedium)

        textPaint.apply {
            color = Color.WHITE
            textSize = 40f
            textAlign = Paint.Align.CENTER
            typeface = font
        }

        val text = "삭제"
        val textX = itemView.right.toFloat() - kotlin.math.abs(maxDx) / 2
        val textY = itemView.top + itemView.height / 2 + textPaint.textSize / 2
        c.drawText(text, textX, textY, textPaint)

        super.onChildDraw(c, recyclerView, viewHolder, maxDx, dY, actionState, isCurrentlyActive)
    }

    private fun initDelete(postId: Int) {
        RetrofitBuilder.api.deletePost(postId = postId, token="Bearer ${App.preferences.token}").enqueue(object: Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Toast.makeText(context, "삭제되었습니다!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}
