package com.example.resepnusantara.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * Class untuk mengelola session user menggunakan SharedPreferences
 */
class SessionManager(context: Context) {
    
    private val prefs: SharedPreferences = 
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREF_NAME = "ResepNusantaraSession"
        private const val KEY_IS_LOGGED_IN = "isLoggedIn"
        private const val KEY_USER_ID = "userId"
        private const val KEY_USERNAME = "username"
        private const val KEY_NAMA_LENGKAP = "namaLengkap"
    }
    
    /**
     * Simpan session login
     */
    fun saveLoginSession(userId: Int, username: String, namaLengkap: String) {
        prefs.edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putInt(KEY_USER_ID, userId)
            putString(KEY_USERNAME, username)
            putString(KEY_NAMA_LENGKAP, namaLengkap)
            apply()
        }
    }
    
    /**
     * Cek apakah user sudah login
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Ambil user ID
     */
    fun getUserId(): Int {
        return prefs.getInt(KEY_USER_ID, 0)
    }
    
    /**
     * Ambil username
     */
    fun getUsername(): String {
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }
    
    /**
     * Ambil nama lengkap
     */
    fun getNamaLengkap(): String {
        return prefs.getString(KEY_NAMA_LENGKAP, "") ?: ""
    }
    
    /**
     * Hapus session (logout)
     */
    fun logout() {
        prefs.edit().clear().apply()
    }
}
