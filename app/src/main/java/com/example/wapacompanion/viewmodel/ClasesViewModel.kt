package com.example.wapacompanion.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapacompanion.data.api.ApiClient
import com.example.wapacompanion.data.model.ClaseModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClasesViewModel : ViewModel() {

    private val _clases = MutableStateFlow<List<ClaseModel>>(emptyList())
    val clases: StateFlow<List<ClaseModel>> = _clases

    fun cargarClases() {
        viewModelScope.launch {
            try {
                val response = ApiClient.clasesService.getClases()
                if (response.isSuccessful) {
                    println(response.body())
                    _clases.value = response.body()?.data ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    fun subirClase(
        pdfPart: MultipartBody.Part,
        nombre: RequestBody,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = ApiClient.clasesService.subirClase(pdfPart, nombre)
                if (response.isSuccessful) {
                    cargarClases()
                    onSuccess()
                } else {
                    onError("Error al subir la clase")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Error desconocido")
            }
        }
    }
}
