package com.example.greendev.view.activity

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.greendev.BindingActivity
import com.example.greendev.R
import com.example.greendev.databinding.ActivityLoginBinding
import com.example.greendev.view.dialog.FinishDialog

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
    }
}