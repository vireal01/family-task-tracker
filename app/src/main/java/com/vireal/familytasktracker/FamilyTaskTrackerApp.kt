package com.vireal.familytasktracker

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.vireal.familytasktracker.navigation.Screens
import com.vireal.familytasktracker.navigation.tasks_flow.nodes.tasks

@Composable
fun FamilyTaskTrackerApp() {
    val navController = rememberNavController()
    FamilyTaskTrackerNavHost(navController = navController)
}

@Composable
fun FamilyTaskTrackerNavHost(
    navController: NavHostController
) {
    NavHost(navController = navController, startDestination = Screens.Tasks.route) {
        tasks(navController)
    }
}
