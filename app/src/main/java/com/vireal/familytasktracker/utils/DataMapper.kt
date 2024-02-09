package com.vireal.familytasktracker.utils

import com.vireal.familytasktracker.data.entities.TaskEntity
import com.vireal.familytasktracker.data.models.TaskModel

fun mapTaskEntityToTaskModel(taskEntity: TaskEntity): TaskModel {
    return TaskModel(
        taskId = taskEntity.taskId,
        taskTitle = taskEntity.taskTitle,
        taskDescription = taskEntity.taskDescription,
        createdDate = taskEntity.createdDate,
        updatedDate = taskEntity.updatedDate,
        assignedToUser = taskEntity.assignedToUser,
        createdByUser = taskEntity.createdByUser,
        dueDate = taskEntity.dueDate,
        isCompleted = taskEntity.isCompleted,
    )
}

fun mapTaskModelToTaskEntity(taskModel: TaskModel): TaskEntity {
    return TaskEntity(
        taskId = taskModel.taskId,
        taskTitle = taskModel.taskTitle,
        taskDescription = taskModel.taskDescription,
        createdDate = taskModel.createdDate,
        updatedDate = taskModel.updatedDate,
        assignedToUser = taskModel.assignedToUser,
        createdByUser = taskModel.createdByUser,
        dueDate = taskModel.dueDate,
        isCompleted = taskModel.isCompleted,
    )
}