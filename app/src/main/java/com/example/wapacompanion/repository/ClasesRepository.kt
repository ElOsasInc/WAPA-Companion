package com.example.wapacompanion.data.repository

import com.example.wapacompanion.data.api.ApiClient
import com.example.wapacompanion.data.model.ClaseModel

class ClasesRepository {

    suspend fun getClases(): List<ClaseModel> {
        val response = ApiClient.clasesService.getClases()

        return if (response.isSuccessful) {
            response.body()?.data ?: emptyList()
        } else {
            emptyList()
        }
    }
}



