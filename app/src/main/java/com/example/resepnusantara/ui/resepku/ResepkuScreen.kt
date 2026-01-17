package com.example.resepnusantara.ui.resepku

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    
    LaunchedEffect(Unit) {
        viewModel.loadUserResep(userId)
    }
    
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(successMessage) {
        successMessage?.let {
            if (it.isNotEmpty()) {
                snackbarHostState.showSnackbar(it)
                viewModel.loadUserResep(userId)
            }
        }
    }
    
    var resepToDelete by remember { mutableStateOf<Resep?>(null) }
    
    if (resepToDelete != null) {
        AlertDialog(
            onDismissRequest = { resepToDelete = null },
            title = { Text("Hapus Resep") },
            text = { Text("Apakah Anda yakin ingin menghapus resep '${resepToDelete?.judul}'?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        resepToDelete?.let { viewModel.deleteResep(it.idResep, userId) }
                        resepToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("HAPUS")
                }
            },
            dismissButton = {
                TextButton(onClick = { resepToDelete = null }) {
                    Text("BATAL")
                }
            }
        )
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resepku") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.AddEditResep.createRoute()) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Tambah Resep")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (!errorMessage.isNullOrEmpty() && userResepList.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = errorMessage!!)
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
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
                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colorScheme.primary)
                            }
                            IconButton(
                                onClick = { resepToDelete = resep },
                                colors = IconButtonDefaults.iconButtonColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Hapus", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}
