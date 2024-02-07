package com.vireal.familytasktracker

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vireal.familytasktracker.navigation.FamilyTaskTrackerNavigationActions
import com.vireal.familytasktracker.navigation.Screens
import com.vireal.familytasktracker.navigation.family_group.nodes.familyGroup
import com.vireal.familytasktracker.navigation.tasks_flow.nodes.tasks
import com.vireal.familytasktracker.ui.AppDrawer
import com.vireal.familytasktracker.ui.theme.FamilyTaskTrackerTheme
import kotlinx.coroutines.launch

@Composable
fun FamilyTaskTrackerApp() {
    FamilyTaskTrackerTheme {

        val navController = rememberNavController()
        val navigationActions = remember(navController) {
            FamilyTaskTrackerNavigationActions(navController)
        }

        val coroutineScope = rememberCoroutineScope()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute =
            navBackStackEntry?.destination?.route ?: Screens.Tasks.route

        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

        ModalNavigationDrawer(
            drawerContent = {
                AppDrawer(
                    currentRoute = currentRoute,
                    navigateToTasks = navigationActions.navigateToTasksScreen,
                    navigateToFamilyGroup = navigationActions.navigateToFamilyGroupScreen,
                    closeDrawer = { coroutineScope.launch { drawerState.close() } }
                )
            },
            drawerState = drawerState
        ) {
            FamilyTaskTrackerNavHost(
                navController = navController,
                openDrawer = { coroutineScope.launch { drawerState.open() } }
            )
        }

    }
}

@Composable
fun FamilyTaskTrackerNavHost(
    navController: NavHostController,
    openDrawer: () -> Unit = {},
) {
    NavHost(navController = navController, startDestination = Screens.Tasks.route) {
        tasks(navController, openDrawer)
        familyGroup(navController, openDrawer)
    }
}
