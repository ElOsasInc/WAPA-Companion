package com.example.wapacompanion.data.model

import com.google.gson.annotations.SerializedName

data class AsistenciaClaseResponse(
    @SerializedName("clase") val clase: ClaseDetalle,
    @SerializedName("alumnos") val alumnos: List<Alumno>,
    @SerializedName("asistencias") val asistencias: List<Asistencia>,
    @SerializedName("fechas") val fechas: List<Fecha>
)

data class ClaseDetalle(
    @SerializedName("id_clase") val idClase: Int,
    @SerializedName("id_materia_fk") val idMateria: String,
    @SerializedName("secuencia") val secuencia: String,
    @SerializedName("periodo") val periodo: String,
    @SerializedName("materia") val materia: String
)

data class Alumno(
    @SerializedName("boleta") val boleta: String,
    @SerializedName("nombre") val nombre: String
)

data class Asistencia(
    @SerializedName("boleta") val boleta: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("asistencia") val asistencia: Boolean,
    @SerializedName("hora") val hora: String?
)

data class Fecha(
    @SerializedName("fecha") val fecha: String
)

data class RegistroAsistenciaRequest(
    @SerializedName("boleta_fk") val boletaFk: String
)

data class ModificarAsistenciaRequest(
    @SerializedName("boleta_fk") val boletaFk: String,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("aof") val aof: Boolean
)