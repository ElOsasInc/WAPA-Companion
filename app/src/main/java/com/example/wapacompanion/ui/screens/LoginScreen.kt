package com.example.wapacompanion.ui.screens

import com.example.wapacompanion.viewmodel.ProfesorViewModel

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginExitoso: () -> Unit
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
            text = "WAPA",
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "Inicio de sesión",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            label = { Text("Usuario") },
            value = profesorViewModel.user,
            //it es el propio valor del campo es como usar self o algo así
            onValueChange = { profesorViewModel.userChange(it) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            label = {Text("Contraseña")},
            value = profesorViewModel.password,
            //it es el propio valor del campo es como usar self o algo así
            onValueChange = { profesorViewModel.passwordChange(it) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                profesorViewModel.login { loginResult ->
                    loginExitoso()
                }
            },
            enabled = !profesorViewModel.isCargando,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text(if (profesorViewModel.isCargando) "Cargando..." else "Iniciar sesión")
        }

        Text(
            text = profesorViewModel.errorMessage,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp),
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }
}