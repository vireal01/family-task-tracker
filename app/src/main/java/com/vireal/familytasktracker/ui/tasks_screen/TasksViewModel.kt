package com.vireal.familytasktracker.ui.tasks_screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vireal.familytasktracker.data.UserPreferencesRepository
import com.vireal.familytasktracker.data.database.AppDatabase
import com.vireal.familytasktracker.data.models.TaskModel
import com.vireal.familytasktracker.utils.mapTaskEntityToTaskModel
import com.vireal.familytasktracker.utils.mapTaskModelToTaskEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val db: AppDatabase,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private var _showCompletedTasks =
        MutableStateFlow(userPreferencesRepository.lastPreferenceWithBlocking)
    val showCompletedTasks: StateFlow<Boolean> = _showCompletedTasks

    private val _allTasksList = MutableStateFlow<List<TaskModel>>(mutableListOf())
    val allTasksList: StateFlow<List<TaskModel>> =
        combine(_allTasksList, _showCompletedTasks) { list, showCompletedTasks ->
            if (showCompletedTasks) {
                list
            } else {
                list.filter { !it.isCompleted }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())


    @SuppressLint("LogNotTimber")
    val exceptionHandler =
        CoroutineExceptionHandler { _, e ->
            Log.e("Error!!", e.toString())
        }

    init {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            db.taskDao().observeTasks().collectLatest { list ->
                _allTasksList.value = list.map { task -> mapTaskEntityToTaskModel(task) }
            }
        }
    }

    fun changeTaskCompletionStatus(taskModel: TaskModel, isCompleted: Boolean) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            val taskEntity = mapTaskModelToTaskEntity(taskModel)
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
            val task = TaskModel(
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
            db.taskDao().insertTask(mapTaskModelToTaskEntity(task))
        }
    }

    fun updateShowCompletedTasks(showCompletedTasks: Boolean) {

        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            userPreferencesRepository.updateShowCompletedTasks(showCompletedTasks)
            _showCompletedTasks.value = showCompletedTasks
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch(Dispatchers.IO + exceptionHandler) {
            db.taskDao().deleteTask(taskId)
        }
    }
}
