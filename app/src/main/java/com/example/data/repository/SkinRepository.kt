package com.example.data.repository

import android.content.Context
import android.graphics.Bitmap
import com.example.data.local.RoutineDao
import com.example.data.local.SkinDatabase
import com.example.data.local.SkinScanDao
import com.example.data.model.RoutineCheckEntity
import com.example.data.model.SkinScanEntity
import com.example.data.remote.GeminiSkinAnalyzer
import com.example.data.remote.RetrofitClient
import com.example.data.remote.SkinAnalysisResult
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SkinRepository(
    private val skinScanDao: SkinScanDao,
    private val routineDao: RoutineDao,
    private val context: Context
) {
    private val analyzer = GeminiSkinAnalyzer()
    private val stringListAdapter = RetrofitClient.moshi.adapter<List<String>>(
        Types.newParameterizedType(List::class.java, String::class.java)
    )

    val allScans: Flow<List<SkinScanEntity>> = skinScanDao.getAllScans()
    val latestScan: Flow<SkinScanEntity?> = skinScanDao.getLatestScan()

    suspend fun getScanById(id: Long): SkinScanEntity? {
        return skinScanDao.getScanById(id)
    }

    suspend fun analyzeAndSaveScan(bitmap: Bitmap): SkinScanEntity {
        // 1. Run AI analysis
        val result = analyzer.analyzeSkin(bitmap)

        // 2. Save bitmap locally to internal storage
        val imagePath = saveBitmapToDisk(bitmap)

        // 3. Map result to entity
        val entity = SkinScanEntity(
            timestamp = System.currentTimeMillis(),
            imageUri = imagePath,
            skinType = result.tipoPiel,
            skinTypeSummary = result.resumenTipoPiel,
            overallScore = result.puntuacionGeneral,
            hydrationScore = result.puntuacionHidratacion,
            smoothnessScore = result.puntuacionTextura,
            uniformityScore = result.puntuacionUniformidad,
            oilControlScore = result.puntuacionControlSebo,
            barrierScore = result.puntuacionBarreraCutanea,
            positiveTraitsJson = stringListAdapter.toJson(result.puntosFortes),
            areasToImproveJson = stringListAdapter.toJson(result.areasMejora),
            howToImproveJson = stringListAdapter.toJson(result.comoMejorar),
            morningRoutineJson = stringListAdapter.toJson(result.rutinaManana),
            nightRoutineJson = stringListAdapter.toJson(result.rutinaNoche),
            weeklyTipsJson = stringListAdapter.toJson(result.consejosSemanales)
        )

        val newId = skinScanDao.insertScan(entity)
        return entity.copy(id = newId)
    }

    suspend fun deleteScan(id: Long) {
        skinScanDao.deleteScanById(id)
    }

    fun parseJsonList(json: String?): List<String> {
        if (json.isNullOrBlank()) return emptyList()
        return try {
            stringListAdapter.fromJson(json) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    fun getTodayKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getRoutineChecksForToday(): Flow<List<RoutineCheckEntity>> {
        return routineDao.getChecksForDate(getTodayKey())
    }

    suspend fun toggleRoutineCheck(stepId: String, isChecked: Boolean) {
        val todayKey = getTodayKey()
        if (isChecked) {
            routineDao.insertOrUpdateCheck(RoutineCheckEntity(dateKey = todayKey, stepId = stepId, isCompleted = true))
        } else {
            routineDao.removeCheck(todayKey, stepId)
        }
    }

    private fun saveBitmapToDisk(bitmap: Bitmap): String {
        return try {
            val fileName = "scan_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            val fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
            fos.flush()
            fos.close()
            file.absolutePath
        } catch (e: Exception) {
            ""
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: SkinRepository? = null

        fun getInstance(context: Context): SkinRepository {
            return INSTANCE ?: synchronized(this) {
                val db = SkinDatabase.getDatabase(context)
                val instance = SkinRepository(db.skinScanDao(), db.routineDao(), context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }
}
