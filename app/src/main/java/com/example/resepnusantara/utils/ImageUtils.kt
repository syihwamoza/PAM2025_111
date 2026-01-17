package com.example.resepnusantara.utils

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Utility class untuk konversi gambar
 */
object ImageUtils {
    
    /**
     * Konversi Bitmap ke Base64 String
     */
    fun bitmapToBase64(bitmap: Bitmap): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
    
    private const val BASE_IMAGE_URL = "http://192.168.0.113/backend/uploads/resep/"
    
    /**
     * Mendapatkan URL lengkap untuk foto resep
     */
    fun getImageUrl(fotoName: String?): String? {
        if (fotoName.isNullOrEmpty()) return null
        return BASE_IMAGE_URL + fotoName
    }
    
    /**
     * Convert URI to Bitmap dengan auto compress
     * @param context Context
     * @param uri URI gambar
     * @param maxWidth Lebar maksimal (default 1024px)
     * @param maxHeight Tinggi maksimal (default 1024px)
     * @return Bitmap yang sudah di-compress
     */
    fun uriToBitmap(
        context: android.content.Context,
        uri: android.net.Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024
    ): Bitmap? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val originalBitmap = android.graphics.BitmapFactory.decodeStream(inputStream)
            inputStream?.close()
            
            // Compress bitmap
            compressBitmap(originalBitmap, maxWidth, maxHeight)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * Compress bitmap ke ukuran maksimal
     */
    private fun compressBitmap(
        bitmap: Bitmap,
        maxWidth: Int,
        maxHeight: Int
    ): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        
        // Hitung scale ratio
        val ratioBitmap = width.toFloat() / height.toFloat()
        val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()
        
        var finalWidth = maxWidth
        var finalHeight = maxHeight
        
        if (ratioMax > ratioBitmap) {
            finalWidth = (maxHeight.toFloat() * ratioBitmap).toInt()
        } else {
            finalHeight = (maxWidth.toFloat() / ratioBitmap).toInt()
        }
        
        return Bitmap.createScaledBitmap(bitmap, finalWidth, finalHeight, true)
    }
    
    /**
     * Convert URI to Base64 (shortcut method)
     */
    fun uriToBase64(
        context: android.content.Context,
        uri: android.net.Uri,
        maxWidth: Int = 1024,
        maxHeight: Int = 1024,
        quality: Int = 80
    ): String? {
        val bitmap = uriToBitmap(context, uri, maxWidth, maxHeight) ?: return null
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}
