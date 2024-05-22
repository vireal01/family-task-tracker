package com.vireal.familytasktracker.ui.family_group_screen

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.vireal.familytasktracker.R
import com.vireal.familytasktracker.ui.common_components.TopAppBar

@Composable
fun FamilyGroupScreen(openDrawer: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(id = R.string.family_group_title),
                openDrawer = { openDrawer() }
            )
        }
    ) {innerPadding ->
        innerPadding
    }
}
