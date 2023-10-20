package com.example.greendev.view.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
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
import com.example.greendev.databinding.FragmentCertificationBinding
import com.example.greendev.model.CertificationBody
import com.example.greendev.model.ImageResponse
import com.example.greendev.view.dialog.BirthPickerDialog
import com.example.greendev.view.dialog.CameraActionListener
import com.example.greendev.view.dialog.DateType
import com.example.greendev.view.dialog.InitDialogData
import com.example.greendev.view.dialog.PhotoDialog
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CertificationFragment(campaignId: Int) : BindingFragment<FragmentCertificationBinding>(R.layout.fragment_certification, false),
    CameraActionListener,
    InitDialogData {
    private var campaignId: Int? = null
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dialog: PhotoDialog
    private lateinit var imageUrl: String
    private val retrofitBuilder = RetrofitBuilder.api
    private var isImageSelected: Boolean = false
    private var isSelectDate: Boolean = false

    init {
        this.campaignId = campaignId
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) {
            if(it != null) {
                dialog.closeDialog()
                binding?.imageView?.setImageURI(it)
                val bitmap = uriToBitmap(requireContext(), it)
                val multipart = bitmapToMultipart(bitmap!!, "test.jpg")
                postImage(multipart)
            }else {
                Log.d("test", "사진 선택 안됨")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.backButton?.let { returnToPreviousFragment(it) }

        binding?.addPhotoButton?.setOnClickListener {
            dialog = PhotoDialog(context as AppCompatActivity, this)
            dialog.initDialog(pickMedia)
        }
        binding?.datePickerButton?.setOnClickListener {
            val dialog = BirthPickerDialog(this, DateType.START)
            dialog.initDialog()
        }
        binding?.postButton?.setOnClickListener {
            initPostButton()
        }
    }

    @SuppressLint("NewApi")
    fun parseToLocalDateTime(input: String): LocalDateTime {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(input, formatter)
        return localDate.atStartOfDay()
    }

    private fun initPostButton() {
        if(checkForm()) {
            RetrofitBuilder.api.postCertification(
                campaignId = campaignId!!,
                token = "Bearer ${App.preferences.token!!}",
                certifiationData = CertificationBody(
                    content = binding?.titleText?.text.toString(),
                    date = parseToLocalDateTime(binding?.datePickerButton?.text.toString()).toString()+":00",
                    postImage = imageUrl
                )
            ).enqueue(object: Callback<JsonObject>{
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Toast.makeText(requireContext(), "참여 인증 되었습니다!", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.popBackStack()
                    requireActivity().supportFragmentManager.popBackStack()
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    TODO("Not yet implemented")
                }
            })
        }else {
            Toast.makeText(requireContext(), "모든 양식을 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkForm(): Boolean = binding?.titleText?.text!!.isNotEmpty() &&
            binding?.description?.text!!.isNotEmpty() &&
            isSelectDate &&
            isImageSelected

    // 이미지 처리 (uri -> bitmap)
    private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BitmapFactory.decodeStream(inputStream)
        }
    }

    // 이미지 처리 (bitmap -> multipart)
    private fun bitmapToMultipart(bitmap: Bitmap, fileName: String): MultipartBody.Part {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()

        val requestBody = byteArray.toRequestBody("image/jpeg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData("file", fileName, requestBody)
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    // 이미지 post
    private fun postImage(body: MultipartBody.Part){
        retrofitBuilder.postImage(file=body).enqueue(object : Callback<ImageResponse> {
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if(response.isSuccessful){
                    Log.d("testtt", response.body()!!.toString())
                    imageUrl = response.body()!!.data.uploadFileUrl
                    isImageSelected = true
                }else{
                    Log.d("testtt error ", "post image : $response")
                }
            }

            override fun onFailure(call: Call<ImageResponse>, t: Throwable) {
                Log.d("testtt", "post image : " + t.message)
            }
        })
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            dialog.closeDialog()
            initCamera()
        } else {
            Toast.makeText(requireContext(), "권한을 거부하였습니다!", Toast.LENGTH_SHORT).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            var bitmap = result.data?.extras?.get("data") as Bitmap
            bitmap = rotateBitmap(bitmap)
            binding?.imageView?.setImageBitmap(bitmap)
            val multipart = bitmapToMultipart(bitmap, "test.jpg")
            postImage(multipart)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun initCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(requireContext().packageManager) != null) {
            takePictureLauncher.launch(takePictureIntent)
        }
    }

    override fun onCameraAction() {
        requestCameraPermission()
    }

    private fun requestCameraPermission() {
        when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) -> {
                dialog.closeDialog()
                initCamera()
            }
            else -> {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    override fun initDialogData(data: String, type: DateType) {
        binding?.datePickerButton?.text = data
        isSelectDate = true
    }
}