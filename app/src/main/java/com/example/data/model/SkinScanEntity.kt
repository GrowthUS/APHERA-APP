package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "skin_scans")
data class SkinScanEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val imageUri: String? = null,
    val skinType: String, // e.g., "Mixta", "Grasa", "Seca", "Normal", "Sensible"
    val skinTypeSummary: String, // Short summary of skin characteristics
    val overallScore: Int, // 0 - 100
    val hydrationScore: Int,
    val smoothnessScore: Int,
    val uniformityScore: Int,
    val oilControlScore: Int,
    val barrierScore: Int,
    val positiveTraitsJson: String, // JSON array string
    val areasToImproveJson: String, // JSON array string
    val howToImproveJson: String, // JSON array string of action steps
    val morningRoutineJson: String, // JSON array string
    val nightRoutineJson: String, // JSON array string
    val weeklyTipsJson: String // JSON array string
)
