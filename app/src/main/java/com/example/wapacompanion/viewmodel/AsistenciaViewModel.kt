package com.example.wapacompanion.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wapacompanion.data.model.*
import com.example.wapacompanion.repository.AsistenciaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class AsistenciaViewModel : ViewModel() {
    private val repository = AsistenciaRepository()

    private val _claseDetalle = MutableStateFlow<ClaseDetalle?>(null)
    val claseDetalle: StateFlow<ClaseDetalle?> = _claseDetalle

    private val _alumnos = MutableStateFlow<List<Alumno>>(emptyList())
    val alumnos: StateFlow<List<Alumno>> = _alumnos

    private val _asistencias = MutableStateFlow<List<Asistencia>>(emptyList())
    val asistencias: StateFlow<List<Asistencia>> = _asistencias

    private val _fechas = MutableStateFlow<List<String>>(emptyList())
    val fechas: StateFlow<List<String>> = _fechas

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    var ultimoCodigo = mutableStateOf("QR - IPN")
    var boletasEscaneadas = mutableStateOf<Set<String>>(emptySet())

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    fun cargarAsistencias(idClase: Int){
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            try{
                val response = repository.getAsistenciasClase(idClase)
                if (response.isSuccessful) {
                    response.body()?.let { data ->
                        _claseDetalle.value = data.clase
                        _alumnos.value = data.alumnos
                        _asistencias.value = data.asistencias
                        _fechas.value = data.fechas.map { it.fecha }
                    }
                } else{
                    val errorMsg = when (response.code()) {
                        403 -> "No tienes permiso para ver esta clase"
                        404 -> "Clase no encontrada"
                        else -> "Error al cargar asistencias: ${response.code()}"
                    }
                    _error.value = errorMsg
                }
            }catch (e: Exception){
                _error.value = "Error de conexion: ${e.message}"
            }finally{
                _loading.value = false
            }
        }
    }

    fun registrarAsistencia(idClase: Int, boleta: String) {
        viewModelScope.launch {
            try {
                val fechaHoy = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                val yaEscaneadaHoy = _asistencias.value.any {
                    it.boleta == boleta && it.fecha == fechaHoy
                }

                if (yaEscaneadaHoy) {
                    _error.value = "Esta boleta ya fue registrada hoy"
                    return@launch
                }

                val alumno = _alumnos.value.find { it.boleta == boleta }
                if (alumno == null) {
                    _error.value = "Alumno no encontrado en esta clase"
                    ultimoCodigo.value = "No encontrado: $boleta"
                    return@launch
                }

                val response = repository.registrarAsistencia(idClase, boleta)
                if (response.isSuccessful) {
                    boletasEscaneadas.value = boletasEscaneadas.value + boleta
                    ultimoCodigo.value = "${alumno.nombre} registrado"

                    cargarAsistencias(idClase)
                } else {
                    _error.value = "Error al registrar asistencia"
                    ultimoCodigo.value = "Error al registrar"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión: ${e.message}"
                ultimoCodigo.value = "Error de conexión"
            }
        }
    }

    fun modificarAsistencia(idClase: Int, boleta: String, fecha: String, asistio: Boolean) {
        viewModelScope.launch {
            try {
                val response = repository.modificarAsistencia(idClase, boleta, fecha, asistio)
                if (response.isSuccessful) {
                    val accion = if (asistio) "presente" else "falta"
                    cargarAsistencias(idClase)
                } else {
                    _error.value = "Error al modificar asistencia"
                }
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
            }
        }
    }

    fun procesarCodigoQR(codigo: String, idClase: Int) {
        viewModelScope.launch {
            try {
                val url = codigo.trim()
                val scrapResponse = repository.scrapHTML(url)
                if (!scrapResponse.isSuccessful) {
                    _error.value = "Error al leer la página del QR"
                    ultimoCodigo.value = "Error en página"
                    return@launch
                }
                val htmlContent = scrapResponse.body()?.html
                if (htmlContent.isNullOrEmpty()) {
                    _error.value = "Página vacía"
                    ultimoCodigo.value = "Página sin contenido"
                    return@launch
                }
                val regex = Regex("([0-9]{2}|PE)[0-9]{8}")
                val match = regex.find(htmlContent)

                if (match != null) {
                    val boleta = match.value.trim()
                    ultimoCodigo.value = "Boleta encontrada: $boleta"
                    registrarAsistencia(idClase, boleta)
                } else {
                    _error.value = "No se encontró boleta en la página"
                    ultimoCodigo.value = "Boleta no encontrada"
                }
            } catch (e: Exception) {
                _error.value = "Error al procesar QR: ${e.message}"
                ultimoCodigo.value = "Error de conexión"
            }
        }
    }

    fun limpiarError() {
        _error.value = null
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}