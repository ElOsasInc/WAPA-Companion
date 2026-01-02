package com.example.wapacompanion.data.api

import com.example.wapacompanion.data.model.ProfesorModel
import com.example.wapacompanion.data.response.SimpleResponse

import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Response
import okhttp3.ResponseBody


interface ProfesorApiService {
    @POST("login")
    suspend fun login(@Body request: ProfesorModel): Response<SimpleResponse>
}