package com.example.greendev.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.greendev.R
import com.example.greendev.databinding.FinishDialogBinding
import com.example.greendev.databinding.PhotoDialogBinding

class PhotoDialog(private val context : FragmentActivity) {
    private lateinit var binding : PhotoDialogBinding
    private val dialog = Dialog(context)

    fun initDialog(pickMedia: ActivityResultLauncher<PickVisualMediaRequest>) {
        binding = PhotoDialogBinding.inflate(context.layoutInflater)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.BOTTOM)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }

        binding.runGallery.setOnClickListener {
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        dialog.show()
    }

    fun closeDialog() = dialog.dismiss()
}