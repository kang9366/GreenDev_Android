package com.example.greendev.view.dialog

import android.app.Dialog
import android.view.Window
import com.example.greendev.databinding.DialogBirthPickerBinding
import sh.tyy.wheelpicker.DatePickerView
import java.util.Calendar
import java.util.Date
import android.view.ViewGroup
import android.view.Gravity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.fragment.app.Fragment

interface InitDialogData {
    fun initDialogData(data: String, type: DateType)
}

enum class DateType {
    START, END
}

class BirthPickerDialog(private val context: Fragment, private val dateType: DateType) {
    private val binding = DialogBirthPickerBinding.inflate(context.layoutInflater)
    private val dialog = Dialog(context.requireContext())
    private val calendar = Calendar.getInstance()

    fun initDialog(){
        setDialog()
        dialog.show()
        initDatePicker()
        initSetButton()
    }

    private fun initSetButton(){
        binding.setButton.setOnClickListener {
            val listener = context as InitDialogData
            listener.initDialogData(binding.dateText.text.toString(), dateType)
            dialog.dismiss()
        }
    }

    private fun initDatePicker(){
        val min = Calendar.getInstance()

        binding.datePicker.apply {
            isHapticFeedbackEnabled = false
            min.set(2003, 8, 14)
            minDate = min.time
            setDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            setWheelListener(object : DatePickerView.Listener {
                override fun didSelectData(year: Int, month: Int, dayOfMonth: Int) {
                    binding.dateText.text = "${year}년 ${month+1}월 ${day}일"
                    var realMonth = "${month+1}"
                    var realDayOfMonth = "$dayOfMonth"

                    if(month+1<10) {
                        realMonth ="0${month+1}"
                    }
                    if (dayOfMonth<10) {
                        realDayOfMonth = "0$dayOfMonth"
                    }

                    val selectDate = "${year}-${realMonth}-${realDayOfMonth}"
                    binding.dateText.text = selectDate
                }
            })
        }
    }

    private fun setDialog(){
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.apply {
                setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                setGravity(Gravity.BOTTOM)
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }
        }
    }
}