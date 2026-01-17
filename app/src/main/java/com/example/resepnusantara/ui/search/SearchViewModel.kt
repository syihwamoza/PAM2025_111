package com.example.resepnusantara.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.Resep
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Search
 */
class SearchViewModel : ViewModel() {
    
    private val repository = ResepRepository()
    
    private val _searchResult = MutableLiveData<List<Resep>>()
    val searchResult: LiveData<List<Resep>> = _searchResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    /**
     * Cari resep berdasarkan query
     */
    fun searchResep(query: String) {
        if (query.isEmpty()) {
            _errorMessage.value = "Masukkan kata kunci pencarian"
            return
        }
        
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = repository.searchResep(query)
                _searchResult.value = result
                
                if (result.isEmpty()) {
                    _errorMessage.value = "Resep tidak ditemukan"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Gagal mencari resep: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
