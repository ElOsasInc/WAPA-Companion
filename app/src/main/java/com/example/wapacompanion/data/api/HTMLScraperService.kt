package com.example.wapacompanion.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

data class ScrapRequest(
    val url: String
)

data class ScrapResponse(
    val html: String
)

interface HTMLScraperService {
    @POST("html-http")
    suspend fun scrapHTML(@Body request: ScrapRequest): Response<ScrapResponse>
}

/*
*
* Esta parte extrae el html para que después sea comparado con la regex de la boleta, inconcluso
*
* */