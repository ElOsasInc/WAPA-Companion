package com.example.wapacompanion.repository

import com.example.wapacompanion.data.api.ApiClient
import com.example.wapacompanion.data.model.ProfesorModel
import com.example.wapacompanion.data.response.SimpleResponse
import com.google.gson.Gson
import com.google.gson.JsonObject

import retrofit2.Response
import okhttp3.ResponseBody

class ProfesorRepository {
    private val profesorService = ApiClient.profesorService

    suspend fun login(request: ProfesorModel): Int {
        try {
            val response: Response<SimpleResponse> = profesorService.login(request)
            return if(response.isSuccessful) 1 else 0
        } catch (e: Exception) {
            return -1
        }

        /*// Imprimir el código HTTP
        println("Código de respuesta: ${response.code()}")
        // Imprimir los headers
        println("Body: ${response.body()}")
        //También puedes ver el error si falla
        println("ErrorBody: ${response.errorBody()?.string()}")*/
    }

    suspend fun logout(): Boolean {
        try {
            val response: Response<SimpleResponse> = profesorService.logout()
            if (response.isSuccessful) ApiClient.clearCookies()
            return response.isSuccessful
        } catch (e: Exception) {
            return false
        }
    }

    suspend fun registrar(nuevoProfesor: ProfesorModel): String {
        try {
            val response: Response<SimpleResponse> = profesorService.registrar(nuevoProfesor)
            val errors = response.errorBody()?.string()
           if(errors != null) {
                val errorsJson = Gson().fromJson(errors, JsonObject::class.java)
                return errorsJson.get("message").asString
            }
            return ""
        } catch (e: Exception) {
            println("error:$e")
            return "No se pudo conectar al servidor"
        }
    }
}