package com.mayeo.sudoku

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import kotlin.math.max

@Composable
fun FixedGrid(columnCount: Int, modifier: Modifier = Modifier, children: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = children,
        measurePolicy = { measurables, constraints ->
            val width = constraints.maxWidth / columnCount
            val placeables = measurables.map {
                it.measure(constraints.copy(minWidth = width, maxWidth = width))
            }
            layout(constraints.maxWidth, constraints.maxHeight) {
                var x = 0
                var y = 0
                var maxHeight = 0
                placeables.forEachIndexed {index, placeable ->
                    if (index % columnCount == 0) {
                        x = 0
                        y = maxHeight
                    }
                    placeable.placeRelative(x, y)
                    x += placeable.width
                    maxHeight = max(maxHeight, y + placeable.height)
                }
            }
        }
    )
}