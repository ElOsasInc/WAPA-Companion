package com.example.wapacompanion.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wapacompanion.viewmodel.ProfesorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavegarLogin: () -> Unit
) {
    val profesorViewModel: ProfesorViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "WAPA - Registro",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )

        OutlinedTextField(
            label = { Text("Usuario") },
            value = profesorViewModel.user,
            onValueChange = { profesorViewModel.userChange(it) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            label = { Text("Correo electrónico") },
            value = profesorViewModel.correo,
            onValueChange = { profesorViewModel.correoChange(it) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            label = { Text("Contraseña") },
            value = profesorViewModel.password,
            onValueChange = { profesorViewModel.passwordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))


        OutlinedTextField(
            label = { Text("Confirmar contraseña") },
            value = profesorViewModel.confirmPassword,
            onValueChange = { profesorViewModel.confirmPasswordChange(it) },
            visualTransformation = PasswordVisualTransformation(),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        )
        Spacer(Modifier.height(16.dp))

        if (profesorViewModel.errorMessage.isNotEmpty()) {
            Text(
                text = profesorViewModel.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 30.dp)
            )
            Spacer(Modifier.height(8.dp))
        }

        Button(
            onClick = {
                profesorViewModel.registrar { registroResult ->
                    onNavegarLogin()
                }
            },
            enabled = !profesorViewModel.isCargando,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
        ) {
            Text(if (profesorViewModel.isCargando) "Registrando..." else "Registrarse")
        }
        Spacer(Modifier.height(16.dp))

        TextButton(
            onClick = {
                onNavegarLogin()
            },
            modifier = Modifier.padding(horizontal = 30.dp)
        ) {
            Text("¿Ya tienes cuenta? Inicia sesión")
        }
    }
}