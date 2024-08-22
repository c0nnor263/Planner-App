package com.conboi.plannerapp.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.conboi.core.data.model.TaskType
import com.conboi.plannerapp.data.dao.TaskDao

@Database(
    version = 2,
    entities = [TaskType::class],
    exportSchema = false,
)
@TypeConverters(TaskDatabaseConvertors::class)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getTaskDao(): TaskDao
}
