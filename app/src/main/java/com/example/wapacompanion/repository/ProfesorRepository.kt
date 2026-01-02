package com.example.wapacompanion.repository

import com.example.wapacompanion.data.api.ApiClient
import com.example.wapacompanion.data.model.ProfesorModel
import com.example.wapacompanion.data.response.SimpleResponse

import retrofit2.Response
import okhttp3.ResponseBody

class ProfesorRepository {
    private val profesorService = ApiClient.profesorService

    suspend fun login(request: ProfesorModel): Boolean {
        val response: Response<SimpleResponse> = profesorService.login(request)

        // Imprimir el código HTTP
        println("Código de respuesta: ${response.code()}")
        // Imprimir los headers
        println("Body: ${response.body()}")
        //También puedes ver el error si falla
        println("ErrorBody: ${response.errorBody()?.string()}")

        return response.isSuccessful
    }
}