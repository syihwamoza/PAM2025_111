package com.example.resepnusantara.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class untuk Favorit
 */
data class Favorit(
    @SerializedName("id_favorit")
    val idFavorit: Int,
    
    @SerializedName("id_resep")
    val idResep: Int,
    
    @SerializedName("id_user")
    val idUser: Int,
    
    @SerializedName("judul")
    val judul: String,
    
    @SerializedName("bahan")
    val bahan: String,
    
    @SerializedName("langkah")
    val langkah: String,
    
    @SerializedName("foto")
    val foto: String?,
    
    @SerializedName("username")
    val username: String,
    
    @SerializedName("created_at")
    val createdAt: String,
    
    @SerializedName("updated_at")
    val updatedAt: String
)

/**
 * Data class untuk Favorit Response
 */
data class FavoritResponse(
    val status: String,
    val message: String,
    val data: List<Favorit>
)

/**
 * Data class untuk Add/Remove Favorit Request
 */
data class FavoritRequest(
    @SerializedName("id_user")
    val idUser: Int,
    
    @SerializedName("id_resep")
    val idResep: Int
)
