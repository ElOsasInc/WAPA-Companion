package com.example.wapacompanion.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wapacompanion.viewmodel.ProfesorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InicioScreen(
    logoutExitoso: () -> Unit
) {
    val profesorViewModel: ProfesorViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Logout",
            style = MaterialTheme.typography.titleLarge
        )

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