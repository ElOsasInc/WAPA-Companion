package com.example.wapacompanion.data.model

import com.google.gson.annotations.SerializedName
data class ClaseModel(

    @SerializedName("ID_Clase")
    val idClase: Int,

    @SerializedName("Materia")
    val materia: String,

    @SerializedName("Secuencia")
    val secuencia: String,

    @SerializedName("Periodo")
    val periodo: String
)
