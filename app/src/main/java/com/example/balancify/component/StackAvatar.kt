package com.example.balancify.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

@Composable
fun StackAvatar(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraint ->
        val placeable = measurables.map { it.measure(constraint) }

        val width = placeable.first().width * (measurables.size * 3 - (measurables.size - 1)) / 3

        val height = placeable.first().measuredHeight

        var x = 0
        layout(width, height) {
            placeable.forEach { placeable ->
                placeable.place(x, 0)

                x += placeable.measuredWidth * 2 / 3
            }
        }
    }
}