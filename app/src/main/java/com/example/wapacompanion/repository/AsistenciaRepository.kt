package com.example.wapacompanion.repository

import com.example.wapacompanion.data.api.ApiClient
import com.example.wapacompanion.data.api.ScrapRequest
import com.example.wapacompanion.data.model.*

class AsistenciaRepository {

    suspend fun getAsistenciasClase(idClase: Int) =
        ApiClient.asistenciaService.getAsistenciasClase(idClase)

    suspend fun registrarAsistencia(idClase: Int, boleta: String) =
        ApiClient.asistenciaService.registrarAsistencia(
            idClase,
            RegistroAsistenciaRequest(boleta)
        )

    suspend fun modificarAsistencia(idClase: Int, boleta: String, fecha: String, asistio: Boolean) =
        ApiClient.asistenciaService.modificarAsistencia(
            idClase,
            ModificarAsistenciaRequest(boleta, fecha, asistio)
        )

    suspend fun scrapHTML(url: String) =
        ApiClient.htmlScraperService.scrapHTML(ScrapRequest(url)
        )
}