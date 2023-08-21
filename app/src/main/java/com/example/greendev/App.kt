package com.example.greendev

import android.app.Application
import com.example.greendev.model.PreferenceUtil

class App: Application() {
    companion object {
        lateinit var preferences: PreferenceUtil
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceUtil(applicationContext)
    }
}