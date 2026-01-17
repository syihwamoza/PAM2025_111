package com.example.resepnusantara.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.SoupKitchen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.resepnusantara.ui.components.RecipeCard
import com.example.resepnusantara.ui.navigation.Screen
import com.example.resepnusantara.utils.SessionManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    val resepList by viewModel.resepList.observeAsState(emptyList())
    val favoritIdList by viewModel.favoritIdList.observeAsState(emptySet())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadInitialData(userId)
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    if (showLogoutDialog) {
        com.example.resepnusantara.ui.components.CuteDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                sessionManager.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0) { inclusive = true }
                }
            },
            title = "Logout?",
            message = "Yakin nih mau keluar dari aplikasi? Nanti rindu resep-resepnya lho..",
            confirmText = "YA",
            dismissText = "TIDAK",
            icon = Icons.Default.ExitToApp,
            iconColor = MaterialTheme.colorScheme.primary
        )
    }
    
    Scaffold(
        topBar = {
            com.example.resepnusantara.ui.components.CustomHeader(
                title = "Halo, ${sessionManager.getNamaLengkap() ?: "User"}",
                subtitle = "Cari resep nusantara versi kamu..",
                titleIcon = Icons.Default.SoupKitchen,
                actions = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { showLogoutDialog = true }
                            .padding(end = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp, 
                            contentDescription = "Logout", 
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Logout",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFFFFF8E1), // Soft Cream
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (isLoading && resepList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!errorMessage.isNullOrEmpty() && resepList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                items(resepList) { resep ->
                    RecipeCard(
                        resep = resep,
                        onClick = { 
                            navController.navigate(Screen.Detail.createRoute(resep.idResep))
                        },
                        isFavorite = favoritIdList.contains(resep.idResep),
                        onFavoriteToggle = {
                            viewModel.toggleFavorite(userId, resep.idResep)
                        }
                    )
                }
            }
        }
    }
}
