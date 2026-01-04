package com.example.manejoasist.ui.screens

import android.annotation.SuppressLint
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import java.text.SimpleDateFormat
import java.util.*

data class RegistroAsistencia(
    val nl: Int,
    val boleta: String,
    val nombre: String,
    val registros: MutableMap<String, String>
)

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun asistenciaScreen() {
    val alumnos = remember {
        mutableStateListOf(
            RegistroAsistencia(12, "2023602253", "ESTRADA ROBLES JOSE ROBERTO", mutableMapOf()),
            RegistroAsistencia(13, "2024600272", "FRAGOSO VAZQUEZ CHRISTOFER AXEL", mutableMapOf()),
            RegistroAsistencia(14, "2024601473", "GALICIA TELLEZ JOSUE", mutableMapOf()),
            RegistroAsistencia(15, "2024600643", "GOMEZ PIÑA IAN", mutableMapOf()),
            RegistroAsistencia(20, "2024600691", "JUAREZ CASTILLO RUBEN GABRIEL", mutableMapOf())
            )
    }

    val fechaHoy = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    Column(modifier = Modifier.fillMaxSize()) {
        QRScanner(
            onCodeScanned = { codigo ->
                val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                val boleta = codigo.lines()
                    .find { it.contains("Boleta") }
                    ?.filter { it.isDigit() }

                val alumno = alumnos.find { it.boleta == boleta }
                if (alumno != null) {
                    alumno.registros[fechaHoy] = horaActual
                }
                val nombre = codigo.lines()
                    .find { it.contains("Nombre") }
                    ?.substringAfter(":")
                    ?.trim()
            }
        )

        Divider()

        TablaAsistencia(alumnos = alumnos, fechas = listOf(fechaHoy))
    }
}

@Composable
fun TablaAsistencia(alumnos: List<RegistroAsistencia>, fechas: List<String>) {
    val scrollState = rememberScrollState()

    Column(modifier = Modifier.horizontalScroll(scrollState).padding(16.dp)) {
        Row {
            Text("N.L.", Modifier.width(50.dp))
            Text("Boleta", Modifier.width(100.dp))
            Text("Nombre", Modifier.width(200.dp))
            fechas.forEach { fecha ->
                Text(fecha, Modifier.width(100.dp))
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        alumnos.forEach { alumno ->
            Row(modifier = Modifier.padding(vertical = 4.dp)) {
                Text("${alumno.nl}", Modifier.width(50.dp))
                Text(alumno.boleta, Modifier.width(100.dp))
                Text(alumno.nombre, Modifier.width(200.dp))
                fechas.forEach { fecha ->
                    val hora = alumno.registros[fecha] ?: ""
                    Box(
                        modifier = Modifier
                            .width(100.dp)
                            .height(30.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(hora)
                    }
                }
            }
        }
    }
}

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun QRScanner(onCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(8.dp),
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val barcodeScanner = BarcodeScanning.getClient()
                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(ContextCompat.getMainExecutor(ctx)) { imageProxy ->
                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {
                                val inputImage = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                barcodeScanner.process(inputImage)
                                    .addOnSuccessListener { barcodes ->
                                        for (barcode in barcodes) {
                                            barcode.rawValue?.let { onCodeScanned(it) }
                                        }
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }
                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}