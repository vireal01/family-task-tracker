package com.vireal.familytasktracker.ui.common_components

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissState
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vireal.familytasktracker.ui.Paddings
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDeleteContainer(
    item: T,
    onDelete: (T) -> Unit,
    content: @Composable (T) -> Unit,
) {
    val dismissStateThreshold = 100.dp
    var isRemoved by remember { mutableStateOf(false) }
    val dismissState =
        rememberDismissState(
            confirmValueChange = { value ->
                if (value == DismissValue.DismissedToStart) {
                    isRemoved = true
                    true
                } else {
                    false
                }
            },
            positionalThreshold = { _ ->
                dismissStateThreshold.toPx()
            },
        )

    val progress = if (dismissState.progress == 1f) 0f else dismissState.progress
    val contentAlpha by animateFloatAsState(
        targetValue = if (dismissState.targetValue == DismissValue.DismissedToEnd) 0f else 1 - progress,
    )

    var backgroundAlpha by remember { mutableStateOf(1f) }
    val backgroundAnimationAlpha by animateFloatAsState(
        targetValue = backgroundAlpha,
        animationSpec = tween(durationMillis = 500), // Adjust the duration as needed
    )

    LaunchedEffect(isRemoved) {
        if (isRemoved) {
            delay(500) // Wait for 0.5 seconds after the item is removed
            backgroundAlpha = 0f
            delay(1000) // Wait for the background fade-out animation to complete
            onDelete(item)
        }
    }
    if (!isRemoved) {
        SwipeToDismiss(
            state = dismissState,
            background = { DeleteBackground(dismissState, backgroundAnimationAlpha) },
            dismissContent = {
                Box(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .alpha(contentAlpha),
                ) {
                    content(item)
                }
            },
            directions = setOf(DismissDirection.EndToStart),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DeleteBackground(
    dismissState: DismissState,
    alpha: Float,
) {
    val color =
        if (dismissState.dismissDirection == DismissDirection.EndToStart) {
            MaterialTheme.colorScheme.error
        } else {
            Color.Transparent
        }

    Surface(
        color = color,
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier.fillMaxSize().alpha(alpha),
    ) {
        val progressOffset =
            if (dismissState.progress == 1f) 0.dp else (dismissState.progress.dp * 100 - 26.dp) * 3
        Log.d("debugg", progressOffset.toString())
        Box(
            modifier =
                Modifier
                    .padding(16.dp)
                    .offset(x = 64.dp) // move Delete text out of the screen
                    .offset(x = -progressOffset)
                    .fillMaxSize(),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = null,
                    tint = Color.White,
                )
                Spacer(modifier = Modifier.width(Paddings.one))
                Text(
                    text = "Delete",
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
