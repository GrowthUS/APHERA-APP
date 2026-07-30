package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.RoutineCheckEntity
import com.example.data.model.SkinScanEntity

@Database(
    entities = [SkinScanEntity::class, RoutineCheckEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SkinDatabase : RoomDatabase() {
    abstract fun skinScanDao(): SkinScanDao
    abstract fun routineDao(): RoutineDao

    companion object {
        @Volatile
        private var INSTANCE: SkinDatabase? = null

        fun getDatabase(context: Context): SkinDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkinDatabase::class.java,
                    "dermai_skin_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
