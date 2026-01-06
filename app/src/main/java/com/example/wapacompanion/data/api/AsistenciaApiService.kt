package com.example.wapacompanion.data.api

import com.example.wapacompanion.data.model.AsistenciaClaseResponse
import com.example.wapacompanion.data.model.ModificarAsistenciaRequest
import com.example.wapacompanion.data.model.RegistroAsistenciaRequest
import com.example.wapacompanion.data.response.SimpleResponse
import retrofit2.Response
import retrofit2.http.*

interface AsistenciaApiService {

    @GET("clases/{id}")
    suspend fun getAsistenciasClase(@Path("id") idClase: Int): Response<AsistenciaClaseResponse>

    @POST("clases/{id}")
    suspend fun registrarAsistencia(
        @Path("id") idClase: Int,
        @Body request: RegistroAsistenciaRequest
    ): Response<SimpleResponse>

    @PUT("clases/{id}")
    suspend fun modificarAsistencia(
        @Path("id") idClase: Int,
        @Body request: ModificarAsistenciaRequest
    ): Response<SimpleResponse>
}