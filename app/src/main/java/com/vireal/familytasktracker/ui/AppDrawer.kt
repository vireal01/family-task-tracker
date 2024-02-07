package com.vireal.familytasktracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.vireal.familytasktracker.navigation.Screens
import com.vireal.familytasktracker.R

@Composable
fun AppDrawer(
    currentRoute: String,
    navigateToTasks: () -> Unit,
    navigateToFamilyGroup: () -> Unit,
    closeDrawer: () -> Unit,
) {
    ModalDrawerSheet {
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.tasks_title)) },
            icon = { Icon(Icons.Default.Check, null) },
            selected = currentRoute == Screens.Tasks.route,
            onClick = { navigateToTasks(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding),
        )
        NavigationDrawerItem(
            label = { Text(stringResource(id = R.string.family_group_title)) },
            icon = { Icon(Icons.Default.Face, null) },
            selected = currentRoute == Screens.FamilyGroup.route,
            onClick = { navigateToFamilyGroup(); closeDrawer() },
            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
        )
    }
}
