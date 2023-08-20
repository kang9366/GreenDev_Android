package com.example.greendev.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import com.example.greendev.App.Companion.preferences
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

        binding.githubLoginButton.setOnClickListener {
            initLogin("github")
        }
        binding.naverLoginButton.setOnClickListener {
            initLogin("naver")
        }
//        수정 필요
        binding.googleLoginButton.setOnClickListener {
            initLogin("google")
        }
        binding.kakaoLoginButton.setOnClickListener {
            initLogin("kakao")
        }
    }


    private fun initLogin(service: String) {
        val webView = WebView(this)
        setContentView(webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val newUrl = request?.url.toString()
                println("URL: $newUrl")
                val accessToken = extractToken(newUrl)
                if(accessToken!=null){
                    Log.d("Token", accessToken.toString())
                    preferences.apply {
                        isLogin = true
                        token = accessToken
                    }

                    Intent(this@LoginActivity, MainActivity::class.java).apply {
                        startActivity(this)
                    }
                    finish()
                }
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        val initialUrl = "https://greendev-api.dev-lr.com/oauth2/authorization/$service?redirect_uri=https://greendev.dev-lr.com"
        webView.loadUrl(initialUrl)

        webView.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP) {
                if (webView.canGoBack()) {
                    webView.goBack()
                    return@setOnKeyListener true
                } else {
                    //webView 종료가 안됨??
                    webView.destroy()
                    return@setOnKeyListener true
                }
            }
            return@setOnKeyListener false
        }
    }

    fun extractToken(url: String): String? {
        val uri = Uri.parse(url)
        return uri.getQueryParameter("accessToken")
    }
}