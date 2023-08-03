package com.example.greendev.view.fragment

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.databinding.FragmentCertificationBinding
import com.example.greendev.view.dialog.PhotoDialog
import java.text.SimpleDateFormat
import java.util.Locale

class CertificationFragment : BindingFragment<FragmentCertificationBinding>(R.layout.fragment_certification, false) {
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dialog: PhotoDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it != null) {
                binding?.imageView?.setImageURI(it)
                dialog.closeDialog()
            }else {
                Log.d("test", "사진 선택 안됨")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }
        binding?.datePickerButton?.setOnClickListener {
            initDatePicker()
        }
        binding?.addPhotoButton?.setOnClickListener {
            dialog = PhotoDialog(context as AppCompatActivity)
            dialog.initDialog(pickMedia)
        }
    }

    private fun initDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    binding?.datePickerButton?.text = setDate(selectedYear, selectedMonth, selectedDay)
                },
                year,
                month,
                day
            )
        }
        datePickerDialog?.show()
    }

    private fun setDate(year: Int, month: Int, day: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, day)
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.format(calendar.time)
    }
}