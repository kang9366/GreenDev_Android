package com.example.greendev.view.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.databinding.FragmentCertificationBinding
import com.example.greendev.view.dialog.CameraActionListener
import com.example.greendev.view.dialog.PhotoDialog
import java.text.SimpleDateFormat
import java.util.Locale

class CertificationFragment : BindingFragment<FragmentCertificationBinding>(R.layout.fragment_certification, false), CameraActionListener {
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dialog: PhotoDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_CODE = 101

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
            dialog = PhotoDialog(context as AppCompatActivity, this)
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

    private fun checkCameraPermission(): Boolean {
        val cameraPermission = Manifest.permission.CAMERA
        val permissionResult = ContextCompat.checkSelfPermission(requireContext(), cameraPermission)
        return permissionResult == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        val cameraPermission = Manifest.permission.CAMERA
        requestPermissions(arrayOf(cameraPermission), CAMERA_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraApp()
            } else {
                // 권한이 거부되었을 때 처리
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCameraApp() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            binding?.imageView?.setImageBitmap(imageBitmap)

        }
    }

    override fun onCameraAction() {
        if(checkCameraPermission()){
            openCameraApp()
        }else{
            requestCameraPermission()
        }
    }
}