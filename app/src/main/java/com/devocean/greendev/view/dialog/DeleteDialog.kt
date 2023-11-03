package com.devocean.greendev.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.devocean.greendev.databinding.DeleteDialogBinding

interface DeleteDialogListener {
    fun onDeleteClicked()
    fun onCancelClicked()
}

class DeleteDialog(private val context : AppCompatActivity, private val listener: DeleteDialogListener) {
    private lateinit var binding : DeleteDialogBinding
    private val dialog = Dialog(context)

    fun initDialog() {
        binding = DeleteDialogBinding.inflate(context.layoutInflater)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.yesButton.setOnClickListener {
            listener.onDeleteClicked()
            dialog.dismiss()
        }

        binding.noButton.setOnClickListener {
            listener.onCancelClicked()
            dialog.dismiss()
        }

        dialog.show()
    }
}