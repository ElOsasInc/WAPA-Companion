package com.example.wapacompanion.viewmodel

import com.example.wapacompanion.repository.ProfesorRepository
import com.example.wapacompanion.data.model.ProfesorModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ProfesorViewModel () : ViewModel() {
    //SE DECLARA EL REPOSITORY PARA ACCEDER A LOS MÉTODOS
    private val profesorRepository = ProfesorRepository()

    //VARIABLES DEL VIEWMODEL PARA MANEJO DESDE LA APP, NO LA API
    var user by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    //VARABLES PARA ESTADO DEL VIEWMODEL
    var isCargando by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    //CAMBIAR LOS VALORES DESDE LAS VIEWS CON FUNCIONES
    fun userChange(nuevoUser: String) {
        user = nuevoUser
    }
    fun passwordChange(nuevaPassword: String) {
        password = nuevaPassword
    }

    //AQUÍ EMPIEZAN LAS FUNCIONES
    fun login(onExitoNavegar: (Boolean) -> Unit) {
        errorMessage = ""
        val request = ProfesorModel(user, "", password)

        viewModelScope.launch {
            isCargando = true
            val loginResult = profesorRepository.login(request)
            errorMessage = when(loginResult) {
                1 -> ""
                0 -> "Usuario no existente"
                else -> "No fue posible conectar al servidor"
            }
            isCargando = false
            if (errorMessage.isEmpty()) onExitoNavegar(true)
        }
    }
}