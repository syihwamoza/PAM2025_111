package com.example.resepnusantara.ui.resepku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.*
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Resepku (CRUD Resep Pribadi)
 */
class ResepkuViewModel : ViewModel() {
    
    private val repository = ResepRepository()
    
    private val _userResepList = MutableLiveData<List<Resep>>()
    val userResepList: LiveData<List<Resep>> = _userResepList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage
    
    /**
     * Load resep milik user
     */
    fun loadUserResep(idUser: Int) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.getUserResep(idUser)
                _userResepList.value = result
                
                if (result.isEmpty()) {
                    _errorMessage.value = "Belum ada resep"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Tambah resep baru
     */
    fun addResep(request: AddResepRequest) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.addResep(request)
                
                if (result != null && result.status == "success") {
                    _successMessage.value = "Resep berhasil ditambahkan"
                } else {
                    _errorMessage.value = result?.message ?: "Gagal menambahkan resep"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Update resep
     */
    fun updateResep(request: UpdateResepRequest) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.updateResep(request)
                
                if (result != null && result.status == "success") {
                    _successMessage.value = "Resep berhasil diupdate"
                } else {
                    _errorMessage.value = result?.message ?: "Gagal mengupdate resep"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Hapus resep
     */
    fun deleteResep(idResep: Int, idUser: Int) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.deleteResep(idResep, idUser)
                
                if (result != null && result.status == "success") {
                    _successMessage.value = "Resep berhasil dihapus"
                } else {
                    _errorMessage.value = result?.message ?: "Gagal menghapus resep"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
