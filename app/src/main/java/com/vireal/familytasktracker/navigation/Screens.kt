package com.vireal.familytasktracker.navigation

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination

sealed class Screens(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList()
) {
    data object Tasks: Screens("tasks")
    data object FamilyGroup: Screens("family_group")
}

class FamilyTaskTrackerNavigationActions(navController: NavController) {
    val navigateToTasksScreen: () -> Unit = {
        navController.navigate(Screens.Tasks.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // reselecting the same item
            launchSingleTop = true
            // Restore state when reselecting a previously selected item
            restoreState = true
        }
    }

    val navigateToFamilyGroupScreen: () -> Unit = {
        navController.navigate(Screens.FamilyGroup.route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
