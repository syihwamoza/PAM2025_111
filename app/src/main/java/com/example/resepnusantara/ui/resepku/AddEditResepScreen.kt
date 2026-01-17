package com.example.resepnusantara.ui.resepku

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
    
    val existingResep by detailViewModel.resep.observeAsState()
    
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
    
    LaunchedEffect(successMessage) {
        successMessage?.let {
            if (it.isNotEmpty()) {
                navController.popBackStack()
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
            TopAppBar(
                title = { Text(if (isEdit) "Edit Resep" else "Tambah Resep") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
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
                onClick = { launcher.launch("image/*") }
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
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = bahan,
                onValueChange = { bahan = it },
                label = { Text("Bahan-bahan") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            OutlinedTextField(
                value = langkah,
                onValueChange = { langkah = it },
                label = { Text("Langkah-langkah") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 5
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
                    .height(50.dp),
                enabled = !isLoading && judul.isNotEmpty() && bahan.isNotEmpty() && langkah.isNotEmpty()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(if (isEdit) "SIMPAN PERUBAHAN" else "TAMBAH RESEP", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
