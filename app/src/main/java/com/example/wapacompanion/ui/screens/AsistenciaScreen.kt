package com.example.wapacompanion.ui.screens

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
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import android.util.Log


data class RegistroAsistencia(
    val nl: Int,
    val boleta: String,
    val nombre: String,
    val registros: MutableMap<String, String>
)

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun AsistenciaScreen() {
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
    val context = LocalContext.current
    var permisoCamara by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        permisoCamara = ContextCompat.checkSelfPermission(
            context,
            android.Manifest.permission.CAMERA
        ) == android.content.pm.PackageManager.PERMISSION_GRANTED
    }

    Column(modifier = Modifier.fillMaxSize()) {
        var ultimoCodigo by remember { mutableStateOf("Esperando escaneo...") }

        Text(
            text = ultimoCodigo,
            modifier = Modifier.padding(8.dp)
        )

        if (permisoCamara) {
            QRScanner(onCodeScanned = { codigo ->
                ultimoCodigo = codigo
                Log.d("QR", "LEIDO: $codigo")

                val boleta = Regex("\\d{10}")
                    .find(codigo)
                    ?.value ?: return@QRScanner

                val alumno = alumnos.find { it.boleta == boleta } ?: return@QRScanner

                if (alumno.registros[fechaHoy] == null) {
                    val horaActual = SimpleDateFormat("HH:mm", Locale.getDefault())
                        .format(Date())

                    alumno.registros[fechaHoy] = horaActual
                }
            }
            )
        }
        else {
            Text(
                "Permiso de cámara NO concedido",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(16.dp)
            )
        }
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

    val cameraExecutor = remember {
        java.util.concurrent.Executors.newSingleThreadExecutor()
    }

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp),
        factory = { ctx ->

            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({

                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }


                val options = BarcodeScannerOptions.Builder()
                    .setBarcodeFormats(
                        Barcode.FORMAT_QR_CODE,
                        Barcode.FORMAT_AZTEC,
                        Barcode.FORMAT_PDF417
                    )
                    .build()

                val barcodeScanner = BarcodeScanning.getClient(options)

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { imageProxy ->

                            val mediaImage = imageProxy.image
                            if (mediaImage != null) {

                                val image = InputImage.fromMediaImage(
                                    mediaImage,
                                    imageProxy.imageInfo.rotationDegrees
                                )

                                barcodeScanner.process(image)
                                    .addOnSuccessListener { barcodes ->
                                        if (barcodes.isNotEmpty()) {
                                            barcodes.first().rawValue?.let {
                                                Log.d("QR", "LEIDO: $it")
                                                onCodeScanned(it)
                                            }
                                        }
                                    }
                                    .addOnFailureListener {
                                        Log.e("QR", "Error MLKit", it)
                                    }
                                    .addOnCompleteListener {
                                        imageProxy.close()
                                    }

                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalyzer
                )

            }, ContextCompat.getMainExecutor(ctx))

            previewView
        }
    )
}