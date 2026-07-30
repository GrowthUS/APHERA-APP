package com.example.ui.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.RoutineCheckEntity
import com.example.data.model.SkinScanEntity
import com.example.data.repository.SkinRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface ScanUiState {
    object Idle : ScanUiState
    data class Analyzing(val stepMessage: String, val progress: Float) : ScanUiState
    data class Success(val scan: SkinScanEntity) : ScanUiState
    data class Error(val message: String) : ScanUiState
}

class SkinViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SkinRepository.getInstance(application)

    val allScans: StateFlow<List<SkinScanEntity>> = repository.allScans
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestScan: StateFlow<SkinScanEntity?> = repository.latestScan
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayRoutineChecks: StateFlow<List<RoutineCheckEntity>> = repository.getRoutineChecksForToday()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _scanUiState = MutableStateFlow<ScanUiState>(ScanUiState.Idle)
    val scanUiState: StateFlow<ScanUiState> = _scanUiState.asStateFlow()

    private val _selectedScanDetail = MutableStateFlow<SkinScanEntity?>(null)
    val selectedScanDetail: StateFlow<SkinScanEntity?> = _selectedScanDetail.asStateFlow()

    fun resetScanState() {
        _scanUiState.value = ScanUiState.Idle
    }

    fun loadScanDetail(scanId: Long) {
        viewModelScope.launch {
            val scan = repository.getScanById(scanId)
                ?: latestScan.value
            _selectedScanDetail.value = scan
        }
    }

    fun analyzeBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            _scanUiState.value = ScanUiState.Analyzing("Iniciando escáner facial...", 0.1f)
            delay(400)
            _scanUiState.value = ScanUiState.Analyzing("Analizando textura de poro e hidratación...", 0.35f)
            delay(500)
            _scanUiState.value = ScanUiState.Analyzing("Evaluando pigmentación y barrera cutánea...", 0.65f)
            delay(500)
            _scanUiState.value = ScanUiState.Analyzing("Generando recomendaciones personalizadas con IA...", 0.85f)

            try {
                val scanResult = repository.analyzeAndSaveScan(bitmap)
                _scanUiState.value = ScanUiState.Success(scanResult)
                _selectedScanDetail.value = scanResult
            } catch (e: Exception) {
                _scanUiState.value = ScanUiState.Error("No se pudo completar el análisis: ${e.message}")
            }
        }
    }

    fun deleteScan(id: Long) {
        viewModelScope.launch {
            repository.deleteScan(id)
            if (_selectedScanDetail.value?.id == id) {
                _selectedScanDetail.value = null
            }
        }
    }

    fun toggleRoutineCheck(stepId: String, isChecked: Boolean) {
        viewModelScope.launch {
            repository.toggleRoutineCheck(stepId, isChecked)
        }
    }

    fun parseJsonList(json: String?): List<String> {
        return repository.parseJsonList(json)
    }

    // Helper to generate realistic preset test bitmaps for emulator testing
    fun createPresetBitmap(presetType: String): Bitmap {
        val width = 600
        val height = 800
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        val paintBg = Paint().apply {
            color = when (presetType) {
                "mixta" -> Color.rgb(250, 235, 230)
                "grasa" -> Color.rgb(245, 240, 225)
                "seca" -> Color.rgb(255, 245, 245)
                else -> Color.rgb(248, 240, 235)
            }
        }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paintBg)

        // Draw face oval representation
        val paintFace = Paint().apply {
            color = Color.rgb(238, 198, 178)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawOval(100f, 150f, 500f, 650f, paintFace)

        // Draw cheeks rose tint
        val paintRose = Paint().apply {
            color = Color.argb(60, 235, 130, 140)
            isAntiAlias = true
        }
        canvas.drawCircle(220f, 440f, 70f, paintRose)
        canvas.drawCircle(380f, 440f, 70f, paintRose)

        // Draw eyes & mouth guides
        val paintFeature = Paint().apply {
            color = Color.rgb(90, 60, 50)
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        canvas.drawCircle(230f, 350f, 20f, paintFeature)
        canvas.drawCircle(370f, 350f, 20f, paintFeature)

        val paintMouth = Paint().apply {
            color = Color.rgb(180, 80, 90)
            style = Paint.Style.STROKE
            strokeWidth = 10f
            strokeCap = Paint.Cap.ROUND
            isAntiAlias = true
        }
        canvas.drawArc(240f, 520f, 360f, 570f, 0f, 180f, false, paintMouth)

        return bitmap
    }
}
