package com.example.data.model

import androidx.room.Entity

@Entity(tableName = "routine_checks", primaryKeys = ["dateKey", "stepId"])
data class RoutineCheckEntity(
    val dateKey: String, // e.g., "2026-07-29"
    val stepId: String,  // e.g., "morning_cleanser"
    val isCompleted: Boolean = true,
    val completedTimestamp: Long = System.currentTimeMillis()
)
