package com.example.resepnusantara.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.Resep
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.launch

/**
 * ViewModel untuk Home/Dashboard
 */
class HomeViewModel : ViewModel() {
    
    private val repository = ResepRepository()
    
    private val _resepList = MutableLiveData<List<Resep>>()
    val resepList: LiveData<List<Resep>> = _resepList
    
    private val _favoritIdList = MutableLiveData<Set<Int>>(emptySet())
    val favoritIdList: LiveData<Set<Int>> = _favoritIdList
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    /**
     * Load semua resep dan favorit user
     */
    fun loadInitialData(idUser: Int?) {
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                // Load all recipes
                val result = repository.getAllResep()
                _resepList.value = result
                
                // Load favorites if user is logged in
                if (idUser != null && idUser != -1) {
                    val favs = repository.getFavoritList(idUser)
                    _favoritIdList.value = favs.map { it.idResep }.toSet()
                }
                
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
     * Toggle status favorit
     */
    fun toggleFavorite(idUser: Int, idResep: Int) {
        val currentFavs = _favoritIdList.value ?: emptySet()
        val isFav = currentFavs.contains(idResep)
        
        viewModelScope.launch {
            try {
                if (isFav) {
                    val response = repository.removeFavorit(idUser, idResep)
                    if (response?.status == "success") {
                        _favoritIdList.value = currentFavs - idResep
                    }
                } else {
                    val response = repository.addFavorit(idUser, idResep)
                    if (response?.status == "success") {
                        _favoritIdList.value = currentFavs + idResep
                    }
                }
            } catch (e: Exception) {
                // Optional: show snackbar error
            }
        }
    }
}
