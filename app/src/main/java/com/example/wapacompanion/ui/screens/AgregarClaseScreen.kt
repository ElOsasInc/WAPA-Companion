package com.example.wapacompanion.ui.screens

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wapacompanion.viewmodel.ClasesViewModel
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun AgregarClaseScreen(
    claseAgregada: () -> Unit
) {


    val clasesViewModel: ClasesViewModel = viewModel()
    val context = LocalContext.current
    val nombreRequestBody = "Clase de prueba"
        .toRequestBody("text/plain".toMediaTypeOrNull())

    var pdfUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        pdfUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Agregar clase")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                launcher.launch(arrayOf("application/pdf"))
            }
        ) {
            Text("Seleccionar PDF")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (pdfUri != null) {
            Text("PDF seleccionado")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            enabled = pdfUri != null,
            onClick = {
                pdfUri?.let { uri ->
                    val part = uriToMultipart(context, uri)

                    clasesViewModel.subirClase(
                        pdfPart = part,
                        nombre = nombreRequestBody,
                        onSuccess = { claseAgregada() },
                        onError = { println(it) }
                    )

                }
            }
        ) {
            Text("Agregar")
        }
    }
}


fun uriToMultipart(
    context: Context,
    uri: Uri
): MultipartBody.Part {

    val inputStream = context.contentResolver.openInputStream(uri)!!
    val bytes = inputStream.readBytes()
    inputStream.close()

    val requestBody = bytes.toRequestBody(
        "application/pdf".toMediaTypeOrNull()
    )

    return MultipartBody.Part.createFormData(
        name = "pdf",
        filename = "clase.pdf",
        body = requestBody
    )
}
