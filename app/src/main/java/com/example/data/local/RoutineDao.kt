package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.RoutineCheckEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {
    @Query("SELECT * FROM routine_checks WHERE dateKey = :dateKey")
    fun getChecksForDate(dateKey: String): Flow<List<RoutineCheckEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCheck(check: RoutineCheckEntity)

    @Query("DELETE FROM routine_checks WHERE dateKey = :dateKey AND stepId = :stepId")
    suspend fun removeCheck(dateKey: String, stepId: String)
}
