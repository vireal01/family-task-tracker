package com.vireal.familytasktracker.ui.tasks_screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.vireal.familytasktracker.ui.Paddings
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.vireal.familytasktracker.R
import com.vireal.familytasktracker.data.models.TaskModel
import com.vireal.familytasktracker.ui.common_components.SwipeToDeleteContainer
import com.vireal.familytasktracker.ui.common_components.TopAppBar

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    openDrawer: () -> Unit = {}
) {
    val showBottomSheet = remember { mutableStateOf(false) }
    val showCompletedTasks = viewModel.showCompletedTasks.collectAsState()
    val tasksList = viewModel.allTasksList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.tasks_title),
                openDrawer = { openDrawer() }
            ) {
                MoreDropdownMenuContent(
                    updateShowCompletedTasks = viewModel::updateShowCompletedTasks,
                    isCompletedTasksShown = showCompletedTasks.value
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    showBottomSheet.value = true
                },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.secondary
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->
        Log.d("debugg", "${tasksList.value.size}")
        if (tasksList.value.isEmpty()) {
            Placeholder()
        } else {
            LazyColumn(
                contentPadding = PaddingValues(Paddings.one),
                verticalArrangement = Arrangement.spacedBy(Paddings.half),
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(tasksList.value, key = { task -> task.taskId }) { task ->
                    Box(modifier = Modifier.animateItemPlacement()) {
                        SwipeToDeleteContainer(
                            item = task,
                            onDelete = {
                                viewModel.deleteTask(task.taskId)
                            }
                        ) {
                            TaskItem(
                                item = task,
                                onCheckedChange = {
                                    viewModel.changeTaskCompletionStatus(
                                        task,
                                        !task.isCompleted
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }

    }
    if (showBottomSheet.value) {
        CreateTaskBottomSheet(
            showBottomSheet = showBottomSheet,
            viewModel = viewModel
        )
    }
}

@Composable
fun TaskItem(
    item: TaskModel,
    onCheckedChange: () -> Unit
) {
    val textDecoration =
        if (item.isCompleted) TextDecoration.LineThrough else TextDecoration.None
    val taskBackgroundColor =
        if (item.isCompleted) MaterialTheme.colorScheme.inverseOnSurface else MaterialTheme.colorScheme.surfaceVariant
    Surface(
        color = taskBackgroundColor,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .padding(Paddings.one)
                    .weight(4f)
                    .height(64.dp - Paddings.one),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = item.taskTitle,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = textDecoration,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = item.taskDescription.toString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = textDecoration,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Left,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.width(Paddings.one))
            }

            Checkbox(
                checked = item.isCompleted,
                onCheckedChange = {
                    onCheckedChange()
                },
                modifier = Modifier.weight(1f)
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTaskBottomSheet(
    showBottomSheet: MutableState<Boolean>,
    viewModel: TasksViewModel
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var taskTitle by rememberSaveable { mutableStateOf("") }
    var taskDescription by rememberSaveable { mutableStateOf("") }


    ModalBottomSheet(onDismissRequest = {
        // TODO: Add alert to prevent false task closing
        showBottomSheet.value = false
    }) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = taskTitle,
                placeholder = {
                    Text(
                        text = "Title",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                },
                textStyle = MaterialTheme.typography.titleMedium,
                onValueChange = { taskTitle = it },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .padding(Paddings.half)
            )
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                placeholder = { Text(text = "Description") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.half)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Paddings.half)
            ) {

                OutlinedButton(
                    onClick = { /*TODO: Open due date picker*/ },
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    Text(
                        text = "Today",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.createTask(
                                taskTitle,
                                taskDescription
                            )
                            sheetState.hide()
                        }.invokeOnCompletion {
                            if (!sheetState.isVisible) {
                                showBottomSheet.value = false
                            }
                        }
                    },
                    enabled = taskTitle.isNotEmpty()
                ) {
                    Icon(Icons.Default.Send, contentDescription = "Create task")
                }
            }

        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }
}


@Composable
fun MoreDropdownMenuContent(
    updateShowCompletedTasks: (Boolean) -> Unit,
    isCompletedTasksShown: Boolean
) {
    var showDropdownMenu by remember { mutableStateOf(false) }

    IconButton(onClick = { showDropdownMenu = true }) {
        Icon(Icons.Default.MoreVert, contentDescription = null)
    }
    DropdownMenu(expanded = showDropdownMenu, onDismissRequest = {
        showDropdownMenu = false
    }) {
        if (isCompletedTasksShown) {
            DropdownMenuItem(
                text = { Text(text = "Hide completed") },
                onClick = { updateShowCompletedTasks(false); showDropdownMenu = false })
        } else {
            DropdownMenuItem(
                text = { Text(text = "Show completed") },
                onClick = { updateShowCompletedTasks(true); showDropdownMenu = false })
        }
    }
}

@Composable
fun Placeholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = "No tasks to do! \n Well done!",
            modifier = Modifier.fillMaxSize(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
