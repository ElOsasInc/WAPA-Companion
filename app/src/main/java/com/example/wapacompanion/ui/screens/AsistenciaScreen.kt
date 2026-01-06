package com.example.wapacompanion.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wapacompanion.viewmodel.AsistenciaViewModel
import com.example.wapacompanion.data.model.Alumno
import com.example.wapacompanion.data.model.Asistencia
import com.example.wapacompanion.ui.components.QRScanner

@Composable
fun AsistenciaScreen(
    idClase: Int,
    viewModel: AsistenciaViewModel = viewModel()
) {
    val claseDetalle by viewModel.claseDetalle.collectAsState()
    val alumnos by viewModel.alumnos.collectAsState()
    val asistencias by viewModel.asistencias.collectAsState()
    val fechas by viewModel.fechas.collectAsState()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    val ultimoCodigo by viewModel.ultimoCodigo
    val boletasEscaneadas by viewModel.boletasEscaneadas

    LaunchedEffect(idClase) {
        viewModel.cargarAsistencias(idClase)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        claseDetalle?.let { clase ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = clase.materia,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${clase.secuencia} - Periodo ${clase.periodo}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .width(200.dp)
                .padding(bottom = 16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                QRScanner(
                    onCodeScanned = { codigoQR ->
                        viewModel.procesarCodigoQR(codigoQR, idClase)
                    }
                )
            }
        }

        error?.let { errorMsg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = errorMsg,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            viewModel.limpiarError()
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }

        mensaje?.let { msg ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = msg,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                    TextButton(
                        onClick = {
                            viewModel.limpiarMensaje()
                        }
                    ) {
                        Text("OK")
                    }
                }
            }
        }

        if (loading) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Text("Cargando asistencias...")
                }
            }
        }

        if (!loading && alumnos.isNotEmpty()) {
            TablaAsistencia(
                alumnos = alumnos,
                asistencias = asistencias,
                fechas = fechas,
                onAsistenciaClick = { boleta, fecha, asistio ->
                    viewModel.modificarAsistencia(idClase, boleta, fecha, !asistio)
                }
            )
        }

        if (!loading && alumnos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No hay alumnos registrados en esta clase",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TablaAsistencia(
    alumnos: List<Alumno>,
    asistencias: List<Asistencia>,
    fechas: List<String>,
    onAsistenciaClick: (String, String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Lista de Asistencia (${alumnos.size} alumnos)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            if (fechas.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    item {
                        Text(
                            text = "Alumno",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(150.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    items(fechas) { fecha ->
                        Text(
                            text = fecha.takeLast(5),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(65.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.height(300.dp)
                ) {
                    items(alumnos) { alumno ->
                        FilaAsistencia(
                            alumno = alumno,
                            fechas = fechas,
                            asistencias = asistencias,
                            onAsistenciaClick = onAsistenciaClick
                        )
                    }
                }
            } else {
                Text(
                    text = "No hay fechas registradas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun FilaAsistencia(
    alumno: Alumno,
    fechas: List<String>,
    asistencias: List<Asistencia>,
    onAsistenciaClick: (String, String, Boolean) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        item {
            Column(
                modifier = Modifier.width(150.dp)
            ) {
                Text(
                    text = alumno.nombre,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = alumno.boleta,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 10.sp
                )
            }
        }

        items(fechas) { fecha ->
            val asistencia = asistencias.find {
                it.boleta == alumno.boleta && it.fecha == fecha
            }

            AsistenciaCell(
                asistencia = asistencia,
                onClick = {
                    asistencia?.let { asist ->
                        onAsistenciaClick(alumno.boleta, fecha, asist.asistencia)
                    }
                }
            )
        }
    }
}

@Composable
private fun AsistenciaCell(
    asistencia: Asistencia?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(65.dp)
            .height(40.dp)
            .clickable { onClick() }
            .border(
                1.dp,
                MaterialTheme.colorScheme.outline,
                RoundedCornerShape(4.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        when {
            asistencia?.asistencia == true -> {
                Text(
                    text = asistencia.hora ?: "??:??",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )
            }
            asistencia?.asistencia == false -> {
                Text(
                    text = "",
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
            else -> {
                Text(
                    text = "-",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}