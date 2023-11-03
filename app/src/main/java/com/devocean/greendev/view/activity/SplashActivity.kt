package com.devocean.greendev.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import com.devocean.greendev.App.Companion.preferences
import com.devocean.greendev.R

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            finish()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.onBackPressedDispatcher.addCallback(this, callback)

        Handler(Looper.getMainLooper()).postDelayed({
            initLoginActivity()
        }, DELAY_TIME)
    }

    private fun initLoginActivity() {
        if(preferences.isLogin){
            Intent(this@SplashActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }else{
            Intent(this@SplashActivity, LoginActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }
    }

    companion object {
        private const val DELAY_TIME = 1500L
    }
}