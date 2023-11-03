package com.devocean.greendev

import android.app.Application
import android.util.Log
import com.devocean.greendev.model.PreferenceUtil
import com.kakao.sdk.common.util.Utility

class App: Application() {
    companion object {
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceUtil(applicationContext)
        val keyHash = Utility.getKeyHash(this)
        Log.d("key hash", keyHash)
    }
}