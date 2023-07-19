package com.example.greendev

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.example.greendev.databinding.FinishDialogBinding

class FinishDialog(private val context : AppCompatActivity) {
    private lateinit var binding : FinishDialogBinding
    private val dialog = Dialog(context)

    fun initDialog() {
        binding = FinishDialogBinding.inflate(context.layoutInflater)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
//        dialog.setCancelable(false)

        binding.yesButton.setOnClickListener {
            context.finish()
        }

        binding.noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}