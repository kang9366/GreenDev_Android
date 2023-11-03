package com.devocean.greendev.view.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import com.devocean.greendev.App.Companion.preferences
import com.devocean.greendev.R
import com.devocean.greendev.databinding.ActivityLoginBinding
import com.devocean.greendev.model.AccessTokenResponse
import com.devocean.greendev.model.LoginBody
import com.devocean.greendev.model.RefreshTokenResponse
import com.devocean.greendev.util.BindingActivity
import com.devocean.greendev.util.RetrofitBuilder
import com.devocean.greendev.view.dialog.FinishDialog
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            val dialog = FinishDialog(this@LoginActivity)
            dialog.initDialog()
        }
    }

    private val oAuthLoginCallback = object : OAuthLoginCallback {
        override fun onSuccess() {
            NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                override fun onSuccess(result: NidProfileResponse) {
                    val userName = result.profile!!.name.toString()
                    val userEmail = result.profile!!.email.toString()
                    val userNickname = result.profile!!.nickname.toString()
                    val userImage = result.profile!!.profileImage

                    Log.d("naver login data", userName)
                    Log.d("naver login data", userEmail)
                    Log.d("naver login data", userNickname)
                    Log.d("naver login data", userImage.toString())

                    requestToken(userEmail, userName, userNickname, userImage.toString())
                }

                override fun onError(errorCode: Int, message: String) {
                    Log.e("login", message)
                }

                override fun onFailure(httpStatus: Int, message: String) {
                    Log.e("login", message)
                }
            })
        }

        override fun onError(errorCode: Int, message: String) {}
        override fun onFailure(httpStatus: Int, message: String) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.login = this
        this.onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getRefreshToken(){
        RetrofitBuilder.api.getRefreshToken().enqueue(object:
            Callback<RefreshTokenResponse> {
            override fun onResponse(call: Call<RefreshTokenResponse>, response: Response<RefreshTokenResponse>) {
                Log.d("refreshtoken", response.body()!!.toString())
                preferences.refreshToken = response.body()!!.data.refreshToken
            }

            override fun onFailure(call: Call<RefreshTokenResponse>, t: Throwable) {
                Log.d("get refresh token error", t.message.toString())
            }
        })
    }

    fun initNaverLogin(view: View?) {
        NaverIdLoginSDK.initialize(this@LoginActivity, "385udoap88VbA7aVDbQa", "jteVFRIjH4", getString(R.string.app_name))
        NaverIdLoginSDK.authenticate(this@LoginActivity, oAuthLoginCallback)
    }

    fun initKakaoLogin(view: View?){
        KakaoSdk.init(this@LoginActivity, "68db8cf69c6b7f6ccc069ecb9d33b365")

        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e("Kakao Login", "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i("Kakao Login", "카카오계정으로 로그인 성공 ${token.accessToken}")
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this@LoginActivity)) {
            UserApiClient.instance.loginWithKakaoTalk(this@LoginActivity) { token, error ->
                if (error != null) {
                    Log.e("Kakao Login", "카카오톡으로 로그인 실패", error)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
                } else if (token != null) {
                    Log.i("Kakao Login", "카카오 로그인 성공 ${token.accessToken}")
//                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
//                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                    finish()
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this@LoginActivity, callback = callback)
        }
    }

    fun initGithubLogin(view: View?) {
        val webView = WebView(this)
        setContentView(webView)
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val newUrl = request?.url.toString()
                println("URL: $newUrl")
                val accessToken = extractToken(newUrl)
                getToken(accessToken)
                return super.shouldOverrideUrlLoading(view, request)
            }
        }

        val initialUrl = "https://greendev-api.dev-lr.com/oauth2/authorization/github?redirect_uri=greendev://open.app"
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

    private fun getToken(accessToken: String?) {
        if(accessToken!=null){
            preferences.apply {
                isLogin = true
                token = accessToken
            }
            getRefreshToken()
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                startActivity(this)
            }
            finish()
        }
    }

    private fun requestToken(email: String, name: String, nickName: String, image: String) {
        RetrofitBuilder.api.naverLogin(LoginBody(email, nickName, name, image)).enqueue(object: Callback<AccessTokenResponse>{
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {
                Log.d("testst", response.toString())
                if(response.isSuccessful){
                    getToken(response.body()!!.data.accessToken)
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun extractToken(url: String): String? {
        val uri = Uri.parse(url)
        return uri.getQueryParameter("accessToken")
    }
}