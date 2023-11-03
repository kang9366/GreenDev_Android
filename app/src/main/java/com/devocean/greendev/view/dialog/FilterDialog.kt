package com.devocean.greendev.view.dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.devocean.greendev.databinding.FilterDialogBinding

class FilterDialog(private val context : AppCompatActivity, private val fragment: Fragment, private val listener: InitDialogData) {
    private lateinit var binding : FilterDialogBinding
    private val dialog = Dialog(context)
    private var buttonClickListener: (() -> Unit)? = null

    fun initDialog() {
        binding = FilterDialogBinding.inflate(context.layoutInflater)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.startDate.setOnClickListener {
            setSDate(binding.startDate)
        }

        binding.endDate.setOnClickListener {
            setSDate(binding.endDate)
        }

        binding.applyButton.setOnClickListener {
            val startDate = binding.startDate.text.toString()
            val endDate = binding.endDate.text.toString()
            if(startDate=="시작 날짜" || endDate == "종료 날짜") {
                Toast.makeText(context, "날짜를 선택해주세요!", Toast.LENGTH_SHORT).show()
            }else {
                val data = "$startDate,$endDate"
                listener.initDialogData(data)
                dialog.dismiss()
            }
        }

        binding.resetButton.setOnClickListener {
            buttonClickListener?.invoke()
            dialog.dismiss()
        }

        dialog.show()
    }

    fun setOnResetListener(listener: () -> Unit) {
        buttonClickListener = listener
    }

    private fun setSDate(button: Button) {
        BirthPickerDialog(fragment, object : InitDialogData {
            override fun initDialogData(data: String) {
                button.text = data
            }
        }).initDialog()
    }
}