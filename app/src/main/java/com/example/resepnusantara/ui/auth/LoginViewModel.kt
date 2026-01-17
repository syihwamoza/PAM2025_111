package com.example.resepnusantara.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.resepnusantara.data.model.LoginResponse
import com.example.resepnusantara.data.repository.ResepRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * ViewModel untuk Login
 */
class LoginViewModel : ViewModel() {
    
    private val repository = ResepRepository()
    
    private val _loginResult = MutableLiveData<LoginResponse?>()
    val loginResult: LiveData<LoginResponse?> = _loginResult
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    /**
     * Fungsi untuk login
     */
    fun login(username: String, password: String) {
        // Validasi input
        if (username.isEmpty() || password.isEmpty()) {
            _errorMessage.value = "Username dan password harus diisi"
            return
        }
        
        _isLoading.value = true
        
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.login(username, password)
                }
                _loginResult.value = result
                
                if (result == null || result.status != "success") {
                    _errorMessage.value = result?.message ?: "Login gagal. Periksa koneksi internet."
                }
            } catch (e: SocketTimeoutException) {
                _errorMessage.value = "Koneksi timeout. Pastikan backend server berjalan di http://10.0.2.2/backend/"
            } catch (e: UnknownHostException) {
                _errorMessage.value = "Tidak dapat terhubung ke server. Periksa koneksi internet atau IP server."
            } catch (e: Exception) {
                _errorMessage.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
