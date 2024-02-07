package com.vireal.familytasktracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.vireal.familytasktracker.data.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    fun observeTasks(): Flow<List<TaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTask(taskEntity: TaskEntity)

    @Update
    fun updateTask(taskEntity: TaskEntity)

    @Query("UPDATE tasks SET is_completed = :isCompleted, updated_date = :updateDate WHERE taskId = :taskId")
    fun updateIsCompleted(taskId: String, isCompleted: Boolean, updateDate: Long = System.currentTimeMillis())
}
