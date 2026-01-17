package com.example.resepnusantara.ui.favorit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.Favorit
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Favorit
 */
class FavoritViewModel : ViewModel() {
    
    private val repository = ResepRepository()
    
    private val _favoritList = MutableLiveData<List<Favorit>>()
    val favoritList: LiveData<List<Favorit>> = _favoritList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    private val _successMessage = MutableLiveData<String>()
    val successMessage: LiveData<String> = _successMessage
    
    /**
     * Load daftar favorit user
     */
    fun loadFavoritList(idUser: Int) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.getFavoritList(idUser)
                _favoritList.value = result
                
                if (result.isEmpty()) {
                    _errorMessage.value = "Belum ada resep favorit"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat data: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    /**
     * Tambah ke favorit
     */
    fun addToFavorit(idUser: Int, idResep: Int) {
        viewModelScope.launch {
            try {
                val result = repository.addFavorit(idUser, idResep)
                
                if (result != null && result.status == "success") {
                    _successMessage.value = "Ditambahkan ke favorit"
                } else {
                    _errorMessage.value = result?.message ?: "Gagal menambahkan ke favorit"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
    
    /**
     * Hapus dari favorit
     */
    fun removeFromFavorit(idUser: Int, idResep: Int) {
        viewModelScope.launch {
            try {
                val result = repository.removeFavorit(idUser, idResep)
                
                if (result != null && result.status == "success") {
                    _successMessage.value = "Dihapus dari favorit"
                } else {
                    _errorMessage.value = result?.message ?: "Gagal menghapus dari favorit"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            }
        }
    }
}
