package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GenerateContentRequest(
    @Json(name = "contents") val contents: List<Content>,
    @Json(name = "generationConfig") val generationConfig: GenerationConfig? = null
)

@JsonClass(generateAdapter = true)
data class Content(
    @Json(name = "parts") val parts: List<Part>
)

@JsonClass(generateAdapter = true)
data class Part(
    @Json(name = "text") val text: String? = null,
    @Json(name = "inlineData") val inlineData: InlineData? = null
)

@JsonClass(generateAdapter = true)
data class InlineData(
    @Json(name = "mimeType") val mimeType: String,
    @Json(name = "data") val data: String
)

@JsonClass(generateAdapter = true)
data class GenerationConfig(
    @Json(name = "temperature") val temperature: Float? = 0.4f,
    @Json(name = "responseMimeType") val responseMimeType: String? = null
)

@JsonClass(generateAdapter = true)
data class GenerateContentResponse(
    @Json(name = "candidates") val candidates: List<Candidate>? = null
)

@JsonClass(generateAdapter = true)
data class Candidate(
    @Json(name = "content") val content: Content? = null
)

// Clean parsed domain representation of the JSON returned by Gemini
@JsonClass(generateAdapter = true)
data class SkinAnalysisResult(
    @Json(name = "tipoPiel") val tipoPiel: String, // e.g. "Piel Mixta con tendencia deshidratada"
    @Json(name = "resumenTipoPiel") val resumenTipoPiel: String,
    @Json(name = "puntuacionGeneral") val puntuacionGeneral: Int, // 0 - 100
    @Json(name = "puntuacionHidratacion") val puntuacionHidratacion: Int,
    @Json(name = "puntuacionTextura") val puntuacionTextura: Int,
    @Json(name = "puntuacionUniformidad") val puntuacionUniformidad: Int,
    @Json(name = "puntuacionControlSebo") val puntuacionControlSebo: Int,
    @Json(name = "puntuacionBarreraCutanea") val puntuacionBarreraCutanea: Int,
    @Json(name = "puntosFortes") val puntosFortes: List<String>, // Lo bueno de tu piel
    @Json(name = "areasMejora") val areasMejora: List<String>,   // Qué necesitas mejorar
    @Json(name = "comoMejorar") val comoMejorar: List<String>,   // Pasos concretos de cómo mejorar
    @Json(name = "rutinaManana") val rutinaManana: List<String>,
    @Json(name = "rutinaNoche") val rutinaNoche: List<String>,
    @Json(name = "consejosSemanales") val consejosSemanales: List<String>
)
