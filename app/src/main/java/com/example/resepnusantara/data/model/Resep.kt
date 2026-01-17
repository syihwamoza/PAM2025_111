package com.example.resepnusantara.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk Resep
 */
data class Resep(
    @SerializedName("id_resep")
    val idResep: Int,
    
    @SerializedName("id_user")
    val idUser: Int,
    
    @SerializedName("judul")
    val judul: String,
    
    @SerializedName("deskripsi")
    val deskripsi: String = "",
    
    @SerializedName("bahan")
    val bahan: String,
    
    @SerializedName("langkah")
    val langkah: String,
    
    @SerializedName("foto")
    val foto: String? = null,
    
    @SerializedName("gambar")
    val gambar: String? = null,
    
    @SerializedName("username")
    val username: String = "",
    
    @SerializedName("created_at")
    val createdAt: String = "",
    
    @SerializedName("updated_at")
    val updatedAt: String = ""
)

/**
 * Data class untuk Resep Response
 */
data class ResepResponse(
    val status: String,
    val message: String,
    val data: List<Resep>
)

/**
 * Data class untuk Add Resep Request
 */
data class AddResepRequest(
    @SerializedName("id_user")
    val idUser: Int,
    
    val judul: String,
    val bahan: String,
    val langkah: String,
    val foto: String? = null
)

/**
 * Data class untuk Update Resep Request
 */
data class UpdateResepRequest(
    @SerializedName("id_resep")
    val idResep: Int,
    
    @SerializedName("id_user")
    val idUser: Int,
    
    val judul: String,
    val bahan: String,
    val langkah: String,
    val foto: String? = null
)

/**
 * Data class untuk Delete Resep Request
 */
data class DeleteResepRequest(
    @SerializedName("id_resep")
    val idResep: Int,
    
    @SerializedName("id_user")
    val idUser: Int
)

/**
 * Data class untuk Add Resep Response
 */
data class AddResepResponse(
    val status: String,
    val message: String,
    val data: ResepId?
)

data class ResepId(
    @SerializedName("id_resep")
    val idResep: Int
)

/**
 * Data class untuk Resep Detail Response
 */
data class ResepDetailResponse(
    val success: Boolean,
    val message: String,
    val data: Resep?
)
