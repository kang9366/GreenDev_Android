package com.example.greendev.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.example.greendev.BindingActivity
import com.example.greendev.R
import com.example.greendev.databinding.ActivityLoginBinding
import com.example.greendev.view.dialog.FinishDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = FinishDialog(this@LoginActivity)
            dialog.initDialog()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        this.onBackPressedDispatcher.addCallback(this, callback)

        binding.googleLoginButton.setOnClickListener {
            binding.load.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.Main).launch {
                binding.load.playAnimation()
                initMainActivity()
            }
        }
    }

    private suspend fun initMainActivity() {
        delay(2000)
        Intent(this@LoginActivity, MainActivity::class.java).apply {
            startActivity(this)
        }
        finish()
    }
}