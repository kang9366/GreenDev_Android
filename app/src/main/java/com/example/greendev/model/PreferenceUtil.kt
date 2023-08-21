package com.example.greendev.model

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    var isLogin: Boolean
        get() = pref.getBoolean("isLogin", false)
        set(value) = pref.edit().putBoolean("isLogin", value).apply()

    var token: String?
        get() = pref.getString("token", null)
        set(value) = pref.edit().putString("token", value).apply()
}