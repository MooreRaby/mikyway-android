package net.pro.mikiway.utils

import android.content.Context
import android.content.SharedPreferences

class SharePreferencesManager (context: Context){

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(Constant.MyPREFERENCES, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    // Save user data
    fun saveUserData(_id: String, usr_name: String, usr_email: String) {
        editor.putString("USER_ID", _id)
        editor.putString("USER_NAME", usr_name)
        editor.putString("USER_EMAIL", usr_email)
        editor.apply()
    }

    // Save tokens
    fun saveTokens(accessToken: String, refreshToken: String) {
        editor.putString("ACCESS_TOKEN", accessToken)
        editor.putString("REFRESH_TOKEN", refreshToken)
        editor.apply()
    }

    // Get user ID
    fun getUserId(): String? {
        return sharedPreferences.getString("USER_ID", null)
    }

    // Get user name
    fun getUserName(): String? {
        return sharedPreferences.getString("USER_NAME", null)
    }

    // Get user email
    fun getUserEmail(): String? {
        return sharedPreferences.getString("USER_EMAIL", null)
    }

    // Get access token
    fun getAccessToken(): String? {
        return sharedPreferences.getString("ACCESS_TOKEN", null)
    }

    // Get refresh token
    fun getRefreshToken(): String? {
        return sharedPreferences.getString("REFRESH_TOKEN", null)
    }

    // Update user data
    fun updateUserData(_id: String?, usr_name: String?, usr_email: String?) {
        _id?.let { editor.putString("USER_ID", it) }
        usr_name?.let { editor.putString("USER_NAME", it) }
        usr_email?.let { editor.putString("USER_EMAIL", it) }
        editor.apply()
    }

    // Clear user data
    fun clearUserData() {
        editor.remove("USER_ID")
        editor.remove("USER_NAME")
        editor.remove("USER_EMAIL")
        editor.remove("ACCESS_TOKEN")
        editor.remove("REFRESH_TOKEN")
        editor.apply()
    }



}