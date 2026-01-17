package com.example.resepnusantara.ui.resep

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resepnusantara.utils.SessionManager
import com.example.resepnusantara.utils.ImageUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    resepId: Int,
    navController: NavController,
    viewModel: DetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    val resep by viewModel.resep.observeAsState()
    val isFavorit by viewModel.isFavorit.observeAsState(false)
    val isLoading by viewModel.isLoading.observeAsState(false)
    
    LaunchedEffect(resepId) {
        viewModel.loadResepDetail(resepId, userId)
    }
    
    Scaffold(
        topBar = {
            com.example.resepnusantara.ui.components.CustomHeader(
                title = "Detail Resep",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = { navController.popBackStack() },
                actions = {
                    IconButton(onClick = { viewModel.toggleFavorit(userId, resepId) }) {
                        Icon(
                            imageVector = if (isFavorit) Icons.Default.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Favorit",
                            tint = if (isFavorit) Color.Red else MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            resep?.let { item ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .verticalScroll(rememberScrollState())
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(24.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        AsyncImage(
                            model = if (item.foto.isNullOrEmpty()) item.gambar else ImageUtils.getImageUrl(item.foto),
                            contentDescription = item.judul,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(250.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = item.judul,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Surface(
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "👩‍🍳 ${item.username}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Bahan Section
                        DetailSectionCard(
                            title = "Bahan-bahan",
                            recipeTitle = item.judul,
                            content = item.bahan,
                            icon = Icons.Default.Restaurant,
                            isNumbered = false
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Langkah Section
                        DetailSectionCard(
                            title = "Langkah Memasak",
                            recipeTitle = item.judul,
                            content = item.langkah,
                            icon = Icons.Default.MenuBook,
                            isNumbered = true
                        )
                        
                }
            } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Resep tidak ditemukan", color = Color.Gray)
            }
        }
    }
}

@Composable
fun DetailSectionCard(title: String, recipeTitle: String = "", content: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isNumbered: Boolean = false) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            if (recipeTitle.isNotEmpty()) {
                Text(
                    text = recipeTitle.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp),
                    letterSpacing = 1.sp
                )
            }
            
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF8D6E63)
                )
            }
            
            Divider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                thickness = 1.dp
            )
            
            // Format content: handle literal \\n and split into lines
            val formattedLines = content.replace("\\\\n", "\n").replace("\\n", "\n").split("\n").filter { it.isNotBlank() }
            
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                formattedLines.forEachIndexed { index, line ->
                    Row {
                        Text(
                            text = if (isNumbered) "${index + 1}." else "•",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.width(24.dp)
                        )
                        Text(
                            text = line.trim().removePrefix("•").trim(),
                            style = MaterialTheme.typography.bodyLarge,
                            lineHeight = 26.sp,
                            color = Color(0xFF5D4037),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}
