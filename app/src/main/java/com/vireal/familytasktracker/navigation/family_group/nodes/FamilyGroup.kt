package com.vireal.familytasktracker.navigation.family_group.nodes

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.vireal.familytasktracker.navigation.Screens
import com.vireal.familytasktracker.ui.family_group_screen.FamilyGroupScreen

fun NavGraphBuilder.familyGroup(
    navController: NavController,
    openDrawer: () -> Unit = {}
) {
    composable(route = Screens.FamilyGroup.route) {
        FamilyGroupScreen(openDrawer = openDrawer)
    }
}
