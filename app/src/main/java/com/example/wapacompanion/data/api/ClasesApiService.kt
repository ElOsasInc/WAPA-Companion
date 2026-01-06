package com.example.wapacompanion.data.api

import com.example.wapacompanion.data.response.ClasesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ClasesApiService {

    @GET("clases")
    suspend fun getClases(): Response<ClasesResponse>

    @Multipart
    @POST("clases")
    suspend fun subirClase(
        @Part pdf: MultipartBody.Part,
        @Part("nombre") nombre: RequestBody
    ): Response<Unit>
}
