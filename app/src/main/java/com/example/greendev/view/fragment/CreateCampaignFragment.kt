package com.example.greendev.view.fragment

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
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
import com.example.greendev.App
import com.example.greendev.BindingFragment
import com.example.greendev.R
import com.example.greendev.RetrofitBuilder
import com.example.greendev.databinding.FragmentCreateCampaignBinding
import com.example.greendev.model.PostCampaign
import com.example.greendev.model.PostCampaignResponse
import com.example.greendev.view.dialog.CameraActionListener
import com.example.greendev.view.dialog.PhotoDialog
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

class CreateCampaignFragment : BindingFragment<FragmentCreateCampaignBinding>(R.layout.fragment_create_campaign, true), CameraActionListener {
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dialog: PhotoDialog
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_CODE = 101
    private val retrofitBuilder = RetrofitBuilder.retrofitService
    private lateinit var image: Bitmap
    private var isSelectStartDate: Boolean = false
    private var isSelectEndDate: Boolean = false


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
        binding?.startDateButton?.setOnClickListener {
            initDatePicker(true)
        }
        binding?.endDateButton?.setOnClickListener {
            initDatePicker(false)
        }

        binding?.addPhotoButton?.setOnClickListener {
            dialog = PhotoDialog(context as AppCompatActivity, this)
            dialog.initDialog(pickMedia)
        }

        binding?.postButton?.setOnClickListener {
            if(checkForm()){
                val campaignData = PostCampaign(
                    title = binding?.campaignTitle?.text.toString(),
                    description = binding?.campaignDescription?.text.toString(),
                    joinCount = 0,
                    joinMemberCount = 0,
                    date = "${binding?.startDateButton?.text.toString()} ~ ${binding?.endDateButton?.text.toString()}",
                    campaignImageUrl = "https://phinf.pstatic.net/contact/20200623_268/1592901094691BqKER_JPEG/image.jpg"
                )
                postCampaign(campaignData)
            }else{
                Toast.makeText(requireContext(), "모든 양식을 입력해주세요!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initDatePicker(isStart: Boolean) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it,
                { _: DatePicker, selectedYear: Int, selectedMonth: Int, selectedDay: Int ->
                    if(isStart){
                        binding?.startDateButton?.text = setDate(selectedYear, selectedMonth, selectedDay)
                        Log.d("testtt", "dateDialog : 날짜 선택함!")
                        isSelectStartDate = true
                    }else{
                        binding?.endDateButton?.text = setDate(selectedYear, selectedMonth, selectedDay)
                        isSelectEndDate = true
                    }
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
                Toast.makeText(requireContext(), "카메라 권한이 필요합니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkForm(): Boolean = binding?.campaignTitle?.text!!.isNotEmpty() &&
            binding?.campaignDescription?.text!!.isNotEmpty() &&
            isSelectStartDate &&
            isSelectEndDate

    private fun openCameraApp() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            image = rotateBitmap(bitmap)
            binding?.imageView?.setImageBitmap(rotateBitmap(image))
        }
    }

    override fun onCameraAction() {
        if(checkCameraPermission()){
            openCameraApp()
        }else{
            requestCameraPermission()
        }
    }

    private fun postImage(body: MultipartBody.Part){
        val postImage: Call<JsonObject> = retrofitBuilder.postImage(body, "Bearer ${App.preferences.token!!}")
        postImage.enqueue(object : Callback<JsonObject>{
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if(response.isSuccessful){
                    Log.d("testtt", "post image : " + response.body().toString())
                }else{
                    Log.d("testtt", "post image : " + response.errorBody()!!.string())
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Log.d("testtt", "post image : " + t.message)
            }
        })
    }

    private fun convertBitmapToFile(bitmap: Bitmap): File {
        val file = File(context?.filesDir, "picture")
        val output = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, output)
        return file
    }

    private fun postCampaign(data: PostCampaign){
        val postCampaign: Call<PostCampaignResponse> = retrofitBuilder.postCampaign("Bearer ${App.preferences.token!!}", data)
        postCampaign.enqueue(object: Callback<PostCampaignResponse>{
            override fun onResponse(call: Call<PostCampaignResponse>, response: Response<PostCampaignResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), "캠페인을 등록하였습니다!", Toast.LENGTH_SHORT).show()
                    Log.d("testtt", "post : " + response.body()!!.data.title)
                    Log.d("testtt", "post : " + response.body()!!.data.description)
                }
            }

            override fun onFailure(call: Call<PostCampaignResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}