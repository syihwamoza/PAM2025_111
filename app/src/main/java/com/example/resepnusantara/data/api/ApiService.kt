package com.example.resepnusantara.data.api

import com.example.resepnusantara.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * Interface API Service untuk Retrofit
 */
interface ApiService {
    
    // ========== Authentication ==========
    
    @POST("login.php")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("register.php")
    suspend fun register(@Body request: RegisterRequest): Response<ApiResponse>
    
    // ========== Resep ==========
    
    @GET("resep_all.php")
    suspend fun getAllResep(): Response<ResepResponse>
    
    @GET("resep_search.php")
    suspend fun searchResep(@Query("query") query: String): Response<ResepResponse>
    
    @GET("resep_user.php")
    suspend fun getUserResep(@Query("id_user") idUser: Int): Response<ResepResponse>
    
    @GET("resep_detail.php")
    suspend fun getResepById(@Query("id") id: Int): Response<ResepDetailResponse>
    
    @POST("resep_add.php")
    suspend fun addResep(@Body request: AddResepRequest): Response<AddResepResponse>
    
    @POST("resep_update.php")
    suspend fun updateResep(@Body request: UpdateResepRequest): Response<ApiResponse>
    
    @POST("resep_delete.php")
    suspend fun deleteResep(@Body request: DeleteResepRequest): Response<ApiResponse>
    
    // ========== Favorit ==========
    
    @GET("favorit_list.php")
    suspend fun getFavoritList(@Query("id_user") idUser: Int): Response<FavoritResponse>
    
    @POST("favorit_add.php")
    suspend fun addFavorit(@Body request: FavoritRequest): Response<ApiResponse>
    
    @POST("favorit_remove.php")
    suspend fun removeFavorit(@Body request: FavoritRequest): Response<ApiResponse>
}
