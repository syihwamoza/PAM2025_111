package com.example.resepnusantara.ui.resep

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.Resep
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    private val repository = ResepRepository()
    
    private val _resep = MutableLiveData<Resep?>()
    val resep: LiveData<Resep?> = _resep
    
    private val _isFavorit = MutableLiveData<Boolean>()
    val isFavorit: LiveData<Boolean> = _isFavorit
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    fun loadResepDetail(id: Int, userId: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val detail = repository.getResepById(id)
                _resep.value = detail
                
                // Check if it's in favorite list
                val favorits = repository.getFavoritList(userId)
                _isFavorit.value = favorits.any { it.idResep == id }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun toggleFavorit(userId: Int, idResep: Int) {
        viewModelScope.launch {
            try {
                if (_isFavorit.value == true) {
                    repository.removeFavorit(userId, idResep)
                    _isFavorit.value = false
                } else {
                    repository.addFavorit(userId, idResep)
                    _isFavorit.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
