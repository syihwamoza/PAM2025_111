package com.example.resepnusantara.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk User
 */
data class User(
    @SerializedName("id_user")
    val idUser: Int,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("nama_lengkap")
    val namaLengkap: String
)

/**
 * Data class untuk Login Request
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Data class untuk Login Response
 */
data class LoginResponse(
    val status: String,
    val message: String,
    val data: User?
)

/**
 * Data class untuk Register Request
 */
data class RegisterRequest(
    val username: String,
    val password: String,
    @SerializedName("nama_lengkap")
    val namaLengkap: String
)

/**
 * Data class untuk Response umum
 */
data class ApiResponse(
    val success: Boolean,
    val status: String? = null,
    val message: String
)
