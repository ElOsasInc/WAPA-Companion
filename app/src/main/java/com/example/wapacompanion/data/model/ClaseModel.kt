package com.example.wapacompanion.data.model

import com.google.gson.annotations.SerializedName

data class ClaseModel(

    @SerializedName("id_clase")
    val idClase: Int,

    @SerializedName("materia")
    val materia: String,

    @SerializedName("secuencia")
    val secuencia: String,

    @SerializedName("periodo")
    val periodo: Int
)