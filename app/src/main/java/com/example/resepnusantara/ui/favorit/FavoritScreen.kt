package com.example.resepnusantara.ui.favorit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.resepnusantara.data.model.Resep
import com.example.resepnusantara.ui.components.RecipeCard
import com.example.resepnusantara.ui.navigation.Screen
import com.example.resepnusantara.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritScreen(
    navController: NavController,
    viewModel: FavoritViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    val favoritList by viewModel.favoritList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadFavoritList(userId)
    }
    
    Scaffold(
        topBar = {
            com.example.resepnusantara.ui.components.CustomHeader(
                title = "Resep Favorit",
                subtitle = "Koleksi masakan kesukaanmu.."
            )
        },
        containerColor = Color(0xFFFFF8E1) // Soft Cream
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (favoritList.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Belum ada resep favorit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Jangan biarkan hati ini kosong! Cari resep yang kamu suka, lalu tekan ikon hati ❤️ untuk menyimpannya di sini.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(favoritList) { favorit ->
                    // Map Favorit to Resep for RecipeCard
                    val resep = Resep(
                        idResep = favorit.idResep,
                        idUser = favorit.idUser,
                        judul = favorit.judul,
                        bahan = favorit.bahan,
                        langkah = favorit.langkah,
                        foto = favorit.foto,
                        username = favorit.username
                    )
                    RecipeCard(
                        resep = resep,
                        onClick = { 
                            navController.navigate(Screen.Detail.createRoute(resep.idResep))
                        },
                        isFavorite = true,
                        onFavoriteToggle = {
                            viewModel.removeFromFavorit(userId, resep.idResep)
                            // Re-load list to reflect change
                            viewModel.loadFavoritList(userId)
                        }
                    )
                }
            }
        }
    }
}
