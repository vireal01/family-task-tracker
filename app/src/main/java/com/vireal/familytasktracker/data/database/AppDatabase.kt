package com.vireal.familytasktracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.vireal.familytasktracker.data.dao.TaskDao
import com.vireal.familytasktracker.data.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun taskDao() : TaskDao
}
