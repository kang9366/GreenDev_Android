package com.devocean.greendev.view.fragment

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
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.devocean.greendev.App
import com.devocean.greendev.R
import com.devocean.greendev.databinding.FragmentCreateCampaignBinding
import com.devocean.greendev.model.ImageResponse
import com.devocean.greendev.model.PostCampaign
import com.devocean.greendev.model.PostCampaignResponse
import com.devocean.greendev.util.BindingFragment
import com.devocean.greendev.util.RetrofitBuilder
import com.devocean.greendev.view.dialog.BirthPickerDialog
import com.devocean.greendev.view.dialog.CameraActionListener
import com.devocean.greendev.view.dialog.InitDialogData
import com.devocean.greendev.view.dialog.PhotoDialog
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream

class CreateCampaignFragment : BindingFragment<FragmentCreateCampaignBinding>(R.layout.fragment_create_campaign, true),
    CameraActionListener {
    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>
    private lateinit var dialog: PhotoDialog
    private lateinit var imageUrl: String
    private val retrofitBuilder = RetrofitBuilder.api
    private var isSelectStartDate: Boolean = false
    private var isSelectEndDate: Boolean = false
    private var isImageSelected: Boolean = false

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
        binding?.startDateButton?.setOnClickListener {
            setSDate(binding?.startDateButton!!)
        }

        binding?.endDateButton?.setOnClickListener {
            setSDate(binding?.endDateButton!!)
        }

        binding?.addPhotoButton?.setOnClickListener {
            dialog = PhotoDialog(context as AppCompatActivity, this)
            dialog.initDialog(pickMedia)
        }

        binding?.postButton?.setOnClickListener {
            createCampaign()
        }
    }

    private fun setSDate(button: Button) {
        BirthPickerDialog(this, object : InitDialogData {
            override fun initDialogData(data: String) {
                button.text = data
            }
        }).initDialog()
    }

    private fun checkForm(): Boolean = binding?.campaignTitle?.text!!.isNotEmpty() &&
            binding?.campaignDescription?.text!!.isNotEmpty() &&
            isSelectStartDate &&
            isSelectEndDate &&
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
        retrofitBuilder.postImage(file=body).enqueue(object : Callback<ImageResponse>{
            override fun onResponse(call: Call<ImageResponse>, response: Response<ImageResponse>) {
                if(response.isSuccessful){
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

    // 캠페인 정보 post
    private fun postCampaign(data: PostCampaign){
        val postCampaign: Call<PostCampaignResponse> = retrofitBuilder.postCampaign("Bearer ${App.preferences.token!!}", data)
        postCampaign.enqueue(object: Callback<PostCampaignResponse>{
            override fun onResponse(call: Call<PostCampaignResponse>, response: Response<PostCampaignResponse>) {
                if(response.isSuccessful){
                    Toast.makeText(requireContext(), "캠페인을 등록하였습니다!", Toast.LENGTH_SHORT).show()
                    val transaction = requireActivity().supportFragmentManager.beginTransaction()
                    transaction.replace(R.id.frameLayout, CreateCampaignFragment())
                    transaction.commit()
                }
            }

            override fun onFailure(call: Call<PostCampaignResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun createCampaign(){
        if(checkForm()){
            val campaignData = PostCampaign(
                title = binding?.campaignTitle?.text.toString(),
                description = binding?.campaignDescription?.text.toString(),
                joinCount = 0,
                joinMemberCount = 0,
                date = "${binding?.startDateButton?.text.toString()} ~ ${binding?.endDateButton?.text.toString()}",
                imageUrl = imageUrl
            )
            postCampaign(campaignData)
        }else{
            Toast.makeText(requireContext(), "모든 양식을 입력해주세요!", Toast.LENGTH_SHORT).show()
        }
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
}