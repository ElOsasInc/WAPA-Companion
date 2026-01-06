package com.example.wapacompanion.data.api

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.HttpUrl

object ApiClient {
    //SI EN ALGUN MOMENTO LO DEPLOYAMOS, ESTO SE CAMBIA
    //private const val BASE_URL = "http://192.168.3.48:8000/"
    private const val BASE_URL = "http://10.0.2.2:8000/api/"

    //ESTO ES PARA EL MANEJO DE LAS COOKIES
    private val cookieJar = object : CookieJar {
        private val cookieStore = mutableMapOf<String, List<Cookie>>()

        override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
            cookieStore[url.host] = cookies

        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookieStore[url.host] ?: emptyList()
        }

        fun clear() {
            cookieStore.clear()
        }
    }

    fun clearCookies() {
        cookieJar.clear()
    }

    private val okHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieJar)
        .build()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val profesorService: ProfesorApiService by lazy {
            retrofit.create(ProfesorApiService::class.java)
    }

    val clasesService: ClasesApiService by lazy {
        retrofit.create(ClasesApiService::class.java)
    }

}