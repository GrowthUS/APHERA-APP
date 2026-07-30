package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SkinScanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SkinScanDao {
    @Query("SELECT * FROM skin_scans ORDER BY timestamp DESC")
    fun getAllScans(): Flow<List<SkinScanEntity>>

    @Query("SELECT * FROM skin_scans ORDER BY timestamp DESC LIMIT 1")
    fun getLatestScan(): Flow<SkinScanEntity?>

    @Query("SELECT * FROM skin_scans WHERE id = :id")
    suspend fun getScanById(id: Long): SkinScanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: SkinScanEntity): Long

    @Query("DELETE FROM skin_scans WHERE id = :id")
    suspend fun deleteScanById(id: Long)

    @Query("DELETE FROM skin_scans")
    suspend fun clearAllScans()
}
