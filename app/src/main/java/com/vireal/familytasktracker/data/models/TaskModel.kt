package com.vireal.familytasktracker.data.models

data class TaskModel(
    val taskId: String,
    val taskTitle: String,
    val taskDescription: String? = null,
    val createdDate: Long,
    val updatedDate: Long,
    val assignedToUser: Int,
    val createdByUser: Int,
    val dueDate: Long? =  null,
    val isCompleted: Boolean,
)
