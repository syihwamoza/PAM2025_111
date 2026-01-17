package com.example.resepnusantara.data.repository

import com.example.resepnusantara.data.api.RetrofitClient
import com.example.resepnusantara.data.model.*

/**
 * Repository untuk mengelola data dari API
 */
class ResepRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // ========== Authentication ==========
    
    suspend fun login(username: String, password: String): LoginResponse? {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun register(username: String, password: String, namaLengkap: String): ApiResponse? {
        return try {
            val response = apiService.register(RegisterRequest(username, password, namaLengkap))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    // ========== Resep ==========
    
    suspend fun getAllResep(): List<Resep> {
        return try {
            val response = apiService.getAllResep()
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun searchResep(query: String): List<Resep> {
        return try {
            val response = apiService.searchResep(query)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun getUserResep(idUser: Int): List<Resep> {
        return try {
            val response = apiService.getUserResep(idUser)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getResepById(id: Int): Resep? {
        return try {
            val response = apiService.getResepById(id)
            if (response.isSuccessful) {
                response.body()?.data
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun addResep(request: AddResepRequest): AddResepResponse? {
        return try {
            val response = apiService.addResep(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun updateResep(request: UpdateResepRequest): ApiResponse? {
        return try {
            val response = apiService.updateResep(request)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun deleteResep(idResep: Int, idUser: Int): ApiResponse? {
        return try {
            val response = apiService.deleteResep(DeleteResepRequest(idResep, idUser))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    // ========== Favorit ==========
    
    suspend fun getFavoritList(idUser: Int): List<Favorit> {
        return try {
            val response = apiService.getFavoritList(idUser)
            if (response.isSuccessful) {
                response.body()?.data ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    
    suspend fun addFavorit(idUser: Int, idResep: Int): ApiResponse? {
        return try {
            val response = apiService.addFavorit(FavoritRequest(idUser, idResep))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    suspend fun removeFavorit(idUser: Int, idResep: Int): ApiResponse? {
        return try {
            val response = apiService.removeFavorit(FavoritRequest(idUser, idResep))
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
