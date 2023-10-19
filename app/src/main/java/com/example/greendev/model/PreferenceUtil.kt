package com.example.greendev.model

import android.content.Context
import android.content.SharedPreferences

class PreferenceUtil(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("pref", Context.MODE_PRIVATE)

    fun clearAll() {
        val editor = pref.edit()
        editor.clear()
        editor.apply()
    }

    var isLogin: Boolean
        get() = pref.getBoolean("isLogin", false)
        set(value) = pref.edit().putBoolean("isLogin", value).apply()

    var token: String?
        get() = pref.getString("token", null)
        set(value) = pref.edit().putString("token", value).apply()

    var refreshToken: String?
        get() = pref.getString("refreshToken", null)
        set(value) = pref.edit().putString("refreshToken", value).apply()
}