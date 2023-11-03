package com.devocean.greendev.view.dialog

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.devocean.greendev.App
import com.devocean.greendev.databinding.LogoutDialogBinding
import com.devocean.greendev.util.RetrofitBuilder
import com.devocean.greendev.view.activity.LoginActivity
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogoutDialog(private val context : AppCompatActivity) {
    private lateinit var binding : LogoutDialogBinding
    private val dialog = Dialog(context)

    fun initDialog() {
        binding = LogoutDialogBinding.inflate(context.layoutInflater)
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(binding.root)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        binding.yesButton.setOnClickListener {
            initLogout()
        }

        binding.noButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun initLogout() {
        val intent = Intent(context, LoginActivity::class.java)
        context.apply {
            startActivity(intent)
            finish()
        }

        RetrofitBuilder.api.logout().enqueue(object: Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                App.preferences.clearAll()
                Toast.makeText(context, "로그아웃 되었습니다!", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}