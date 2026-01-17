package com.example.resepnusantara.ui.resepku

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.resepnusantara.data.model.AddResepRequest
import com.example.resepnusantara.data.model.UpdateResepRequest
import com.example.resepnusantara.ui.resep.DetailViewModel
import com.example.resepnusantara.utils.ImageUtils
import com.example.resepnusantara.utils.SessionManager
import com.example.resepnusantara.ui.components.CuteDialog
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditResepScreen(
    resepId: Int,
    navController: NavController,
    viewModel: ResepkuViewModel = viewModel(),
    detailViewModel: DetailViewModel = viewModel()
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val userId = sessionManager.getUserId()
    
    var judul by remember { mutableStateOf("") }
    var bahan by remember { mutableStateOf("") }
    var langkah by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var base64Image by remember { mutableStateOf<String?>(null) }
    var currentImageUrl by remember { mutableStateOf<String?>(null) }
    
    val isEdit = resepId != -1
    val isLoading by viewModel.isLoading.observeAsState(false)
    val successMessage by viewModel.successMessage.observeAsState()
    val errorMessage by viewModel.errorMessage.observeAsState()
    val existingResep by detailViewModel.resep.observeAsState()
    
    var showDialog by remember { mutableStateOf(false) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }
    var dialogIcon by remember { mutableStateOf(Icons.Default.Restaurant) }
    var isSuccessAction by remember { mutableStateOf(false) }
    
    LaunchedEffect(resepId) {
        if (isEdit) {
            detailViewModel.loadResepDetail(resepId, userId)
        }
    }
    
    LaunchedEffect(existingResep) {
        existingResep?.let {
            judul = it.judul
            bahan = it.bahan
            langkah = it.langkah
            currentImageUrl = if (it.foto.isNullOrEmpty()) it.gambar else ImageUtils.getImageUrl(it.foto)
        }
    }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            if (it.isNotEmpty()) {
                isSuccessAction = false
                dialogTitle = "Kesalahan"
                dialogMessage = it
                dialogIcon = Icons.Default.Error
                showDialog = true
                viewModel.clearErrorMessage()
            }
        }
    }
    
    LaunchedEffect(successMessage) {
        successMessage?.let {
            if (it.isNotEmpty()) {
                isSuccessAction = true
                dialogTitle = "Berhasil!"
                dialogMessage = it
                dialogIcon = Icons.Default.CheckCircle
                showDialog = true
            }
        }
    }
    
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            base64Image = ImageUtils.uriToBase64(context, it)
        }
    }
    
    Scaffold(
        topBar = {
            com.example.resepnusantara.ui.components.CustomHeader(
                title = if (isEdit) "Edit Resep" else "Tambah Resep",
                navigationIcon = Icons.Default.ArrowBack,
                onNavigationClick = { navController.popBackStack() }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Preview Image
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                shape = RoundedCornerShape(28.dp),
                onClick = { launcher.launch("image/*") },
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else if (currentImageUrl != null) {
                        AsyncImage(
                            model = currentImageUrl,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Klik untuk pilih foto", color = MaterialTheme.colorScheme.secondary)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = judul,
                onValueChange = { judul = it },
                label = { Text("Judul Resep") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = bahan,
                onValueChange = { bahan = it },
                label = { Text("Bahan-bahan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = langkah,
                onValueChange = { langkah = it },
                label = { Text("Langkah-langkah") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5,
                shape = RoundedCornerShape(24.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Button(
                onClick = {
                    if (isEdit) {
                        viewModel.updateResep(
                            UpdateResepRequest(
                                idResep = resepId,
                                idUser = userId,
                                judul = judul,
                                bahan = bahan,
                                langkah = langkah,
                                foto = base64Image
                            )
                        )
                    } else {
                        viewModel.addResep(
                            AddResepRequest(
                                idUser = userId,
                                judul = judul,
                                bahan = bahan,
                                langkah = langkah,
                                foto = base64Image
                            )
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                enabled = !isLoading && judul.isNotEmpty() && bahan.isNotEmpty() && langkah.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (isEdit) "SIMPAN PERUBAHAN" else "TAMBAH RESEP", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }

    if (showDialog) {
        CuteDialog(
            title = dialogTitle,
            message = dialogMessage,
            onDismiss = { 
                showDialog = false 
                if (isSuccessAction) navController.popBackStack()
            },
            onConfirm = { 
                showDialog = false 
                if (isSuccessAction) navController.popBackStack()
            },
            confirmText = "MANTAP",
            icon = dialogIcon
        )
    }
}
