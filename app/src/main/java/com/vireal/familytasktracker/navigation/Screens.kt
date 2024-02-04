package com.vireal.familytasktracker.navigation

import androidx.navigation.NamedNavArgument

sealed class Screens(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList()
) {
    data object Tasks: Screens("tasks")
}