package com.vireal.familytasktracker.navigation.tasks_flow.nodes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vireal.familytasktracker.navigation.Screens
import com.vireal.familytasktracker.ui.tasks_screen.TaskScreen

fun NavGraphBuilder.tasks(
    navController: NavController,
    openDrawer: () -> Unit = {},
) {
    composable(route = Screens.Tasks.route) {
        TaskScreen(openDrawer = openDrawer)
    }
}
