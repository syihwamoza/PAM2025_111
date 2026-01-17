package com.example.resepnusantara.ui.resepku

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
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
fun ResepkuScreen(
    navController: NavController,
    viewModel: ResepkuViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    val userResepList by viewModel.userResepList.observeAsState(emptyList())
    val isLoading by viewModel.isLoading.observeAsState(false)
    val errorMessage by viewModel.errorMessage.observeAsState()
    val successMessage by viewModel.successMessage.observeAsState()
    
    var showSuccessDialog by remember { mutableStateOf(false) }
    var successMsg by remember { mutableStateOf("") }
    
    LaunchedEffect(Unit) {
        viewModel.loadUserResep(userId)
    }
    
    LaunchedEffect(successMessage) {
        successMessage?.let {
            if (it.isNotEmpty()) {
                successMsg = it
                showSuccessDialog = true
                viewModel.loadUserResep(userId)
            }
        }
    }
    
    var resepToDelete by remember { mutableStateOf<Resep?>(null) }
    
    if (resepToDelete != null) {
        com.example.resepnusantara.ui.components.CuteDialog(
            onDismiss = { resepToDelete = null },
            onConfirm = {
                resepToDelete?.let { viewModel.deleteResep(it.idResep, userId) }
                resepToDelete = null
            },
            title = "Hapus Resep?",
            message = "Apakah kamu yakin ingin menghapus resep '${resepToDelete?.judul}'? Data yang dihapus tidak bisa dikembalikan lho.",
            confirmText = "HAPUS",
            dismissText = "BATAL",
            icon = Icons.Default.Delete,
            iconColor = MaterialTheme.colorScheme.error
        )
    }
    
    Scaffold(
        topBar = {
            com.example.resepnusantara.ui.components.CustomHeader(
                title = "Resepku",
                subtitle = "Catatan resep rahasiamu.."
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditResep.createRoute()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Resep")
            }
        },
        containerColor = Color(0xFFFFF8E1) // Soft Cream
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (userResepList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(100.dp),
                        tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Belum ada resep buatanmu nih..",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "Yuk mulai berkreasi!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(userResepList) { resep ->
                        Box {
                            RecipeCard(
                                resep = resep,
                                onClick = { 
                                    navController.navigate(Screen.Detail.createRoute(resep.idResep))
                                }
                            )
                            Row(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(8.dp)
                            ) {
                                IconButton(
                                    onClick = { navController.navigate(Screen.AddEditResep.createRoute(resep.idResep)) },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(0xFF2196F3), // Vibrant Blue
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Edit, contentDescription = "Edit", modifier = Modifier.size(20.dp))
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                IconButton(
                                    onClick = { resepToDelete = resep },
                                    colors = IconButtonDefaults.iconButtonColors(
                                        containerColor = Color(0xFFF44336), // Vibrant Red
                                        contentColor = Color.White
                                    ),
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Hapus", modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        com.example.resepnusantara.ui.components.CuteDialog(
            title = "Berhasil!",
            message = successMsg,
            onDismiss = { showSuccessDialog = false },
            onConfirm = { showSuccessDialog = false },
            confirmText = "OKE",
            icon = Icons.Default.CheckCircle
        )
    }
}
