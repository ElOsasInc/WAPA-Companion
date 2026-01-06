package com.example.wapacompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wapacompanion.viewmodel.ClasesViewModel
import com.example.wapacompanion.viewmodel.ProfesorViewModel
import androidx.compose.runtime.getValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    agregarClase: () -> Unit,
    logoutExitoso: () -> Unit
    verDetallesClase: () -> Unit
) {
    val profesorViewModel: ProfesorViewModel = viewModel()
    val clasesViewModel: ClasesViewModel = viewModel()
    val clases by clasesViewModel.clases.collectAsState()

    LaunchedEffect(Unit) {
        clasesViewModel.cargarClases()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text(
            text = "Mis clases",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (clases.isEmpty()) {
            Text("Aún no tienes clases")
            Spacer(modifier = Modifier.height(10.dp))
            } else {
            LazyColumn {
                items(clases) { clase ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Text(
                            text = "${clase.materia} - ${clase.secuencia}",
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }

        Button(
            onClick = { agregarClase() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Agregar clase")
        }

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            onClick = {
                verDetallesClase()
                /*profesorViewModel.logout { logoutResult ->
                    logoutExitoso()
                }*/
            },
            enabled = true,/*!profesorViewModel.isCargando,*/
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Ver clase muestra")
        }

        Button(
            onClick = {
                profesorViewModel.logout { logoutResult ->
                    logoutExitoso()
                }
            },
            enabled = !profesorViewModel.isCargando,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text("Cerrar sesión")
        }
    }
}