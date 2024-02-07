package com.vireal.familytasktracker.ui.tasks_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.familytasktracker.data.database.AppDatabase
import com.vireal.familytasktracker.data.entities.TaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val db: AppDatabase
) : ViewModel() {

    private val _allTasksList = MutableStateFlow<List<TaskEntity>>(mutableListOf())
    val allTasksList: StateFlow<List<TaskEntity>> = _allTasksList

    @SuppressLint("LogNotTimber")
    val exceptionHandler =
        CoroutineExceptionHandler { _, e ->
            Log.e("Error!!", e.toString())
        }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            db.taskDao().observeTasks().collectLatest {
                _allTasksList.value = it
            }
        }
    }

    fun changeTaskCompletionStatus(taskEntity: TaskEntity, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            db.taskDao().updateIsCompleted(taskEntity.taskId, isCompleted)
        }
    }

    fun createTask(
        taskTitle: String,
        // Should i limit title and description fields max length?
        taskDescription: String?,
        dueDate: Long? = null
    ) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val task = TaskEntity(
                taskId = UUID.randomUUID().toString(),
                taskTitle = taskTitle,
                taskDescription = taskDescription,
                createdDate = System.currentTimeMillis(),
                updatedDate = System.currentTimeMillis(),
                assignedToUser = 1,
                createdByUser = 1,
                dueDate = dueDate,
                isCompleted = false
            )
            db.taskDao().insertTask(task)
        }
    }
}
