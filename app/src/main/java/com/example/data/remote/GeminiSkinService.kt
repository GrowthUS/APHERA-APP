package com.example.data.remote

import android.graphics.Bitmap
import android.util.Base64
import com.example.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    val service: GeminiApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(GeminiApiService::class.java)
    }
}

class GeminiSkinAnalyzer {

    suspend fun analyzeSkin(bitmap: Bitmap): SkinAnalysisResult = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY.trim()
        
        // If API key is missing or standard placeholder, fallback gracefully
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey.startsWith("MY_")) {
            return@withContext FallbackSkinAnalyzer.generateReport(bitmap)
        }

        try {
            val base64Image = bitmapToBase64(bitmap)
            val systemPrompt = """
                Eres un dermatólogo especialista y cosmetólogo clínico. Analiza la imagen facial adjunta para realizar una evaluación dermatológica exhaustiva en ESPAÑOL.
                Debes responder STRICTLY en formato JSON válido con la siguiente estructura (sin bloques markdown extras):
                {
                  "tipoPiel": "Nombre del tipo de piel (Grasa, Seca, Mixta, Normal o Sensible)",
                  "resumenTipoPiel": "Descripción dermatológica breve del tipo de piel detectado.",
                  "puntuacionGeneral": 85,
                  "puntuacionHidratacion": 80,
                  "puntuacionTextura": 88,
                  "puntuacionUniformidad": 82,
                  "puntuacionControlSebo": 75,
                  "puntuacionBarreraCutanea": 86,
                  "puntosFortes": [
                    "Lo positivo 1 (ej. Excelente firmeza en pómulos)",
                    "Lo positivo 2 (ej. Poca hiperpigmentación visibile)",
                    "Lo positivo 3 (ej. Buena elasticidad general)"
                  ],
                  "areasMejora": [
                    "Área a mejorar 1 (ej. Poros dilatados en la zona T)",
                    "Área a mejorar 2 (ej. Ligera deshidratación periorbital)",
                    "Área a mejorar 3 (ej. Enrojecimiento sutil en aletas de la nariz)"
                  ],
                  "comoMejorar": [
                    "Recomendación 1: Usa suero con Niacinamida 10% para regular el sebo y minimizar poros.",
                    "Recomendación 2: Incorpora Ácido Hialurónico de bajo peso molecular para hidratación profunda.",
                    "Recomendación 3: Usa protector solar fluido SPF 50+ toque seco diariamente."
                  ],
                  "rutinaManana": [
                    "1. Limpiador gel suave pH 5.5",
                    "2. Sérum antioxidante Vitamina C 10%",
                    "3. Hidratante ligero con Ceramidas",
                    "4. Protector Solar SPF 50+"
                  ],
                  "rutinaNoche": [
                    "1. Doble limpieza (aceite limpiador + limpiador acuoso)",
                    "2. Tónico calmante con Centella Asiática",
                    "3. Sérum con Niacinamida y Ácido Hialurónico",
                    "4. Crema reparadora nocturna"
                  ],
                  "consejosSemanales": [
                    "Exfoliación suave con BHA 2% (Ácido Salicílico) 2 veces por semana.",
                    "Mantén un consumo de agua de al menos 2 litros al día.",
                    "Cambia la funda de almohada de seda/algodón dos veces por semana."
                  ]
                }
            """.trimIndent()

            val request = GenerateContentRequest(
                contents = listOf(
                    Content(
                        parts = listOf(
                            Part(text = systemPrompt),
                            Part(inlineData = InlineData(mimeType = "image/jpeg", data = base64Image))
                        )
                    )
                ),
                generationConfig = GenerationConfig(
                    temperature = 0.3f,
                    responseMimeType = "application/json"
                )
            )

            val response = RetrofitClient.service.generateContent(apiKey, request)
            val jsonText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text

            if (jsonText.isNullOrBlank()) {
                return@withContext FallbackSkinAnalyzer.generateReport(bitmap)
            }

            val cleanJson = cleanJsonResponse(jsonText)
            val adapter = RetrofitClient.moshi.adapter(SkinAnalysisResult::class.java)
            val parsedResult = adapter.fromJson(cleanJson)

            parsedResult ?: FallbackSkinAnalyzer.generateReport(bitmap)

        } catch (e: Exception) {
            e.printStackTrace()
            FallbackSkinAnalyzer.generateReport(bitmap)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        // Resize bitmap if very large to prevent memory and network payload issues
        val scaledBitmap = if (bitmap.width > 1024 || bitmap.height > 1024) {
            val scale = 1024f / maxOf(bitmap.width, bitmap.height)
            Bitmap.createScaledBitmap(bitmap, (bitmap.width * scale).toInt(), (bitmap.height * scale).toInt(), true)
        } else {
            bitmap
        }
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        return Base64.encodeToString(outputStream.toByteArray(), Base64.NO_WRAP)
    }

    private fun cleanJsonResponse(raw: String): String {
        var trimmed = raw.trim()
        if (trimmed.startsWith("```json")) {
            trimmed = trimmed.removePrefix("```json")
        }
        if (trimmed.startsWith("```")) {
            trimmed = trimmed.removePrefix("```")
        }
        if (trimmed.endsWith("```")) {
            trimmed = trimmed.removeSuffix("```")
        }
        return trimmed.trim()
    }
}
