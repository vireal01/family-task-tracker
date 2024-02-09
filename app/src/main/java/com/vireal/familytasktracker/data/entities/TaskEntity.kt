package com.vireal.familytasktracker.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey val taskId: String,
    @ColumnInfo(name = "title") val taskTitle: String,
    @ColumnInfo(name = "description") val taskDescription: String?,
    @ColumnInfo(name = "created_date") val createdDate: Long,
    @ColumnInfo(name = "updated_date") val updatedDate: Long,
    @ColumnInfo(name = "assigned_to_user") val assignedToUser: Int,
    @ColumnInfo(name = "created_by_user") val createdByUser: Int,
    @ColumnInfo(name = "due_date") val dueDate: Long?,
    @ColumnInfo(name = "is_completed") val isCompleted: Boolean,
    // make new table assignees assignee has own id and tasks
    // 1/ users 2/ tasks 3/ many-to-many relations taskId - assignee
)
