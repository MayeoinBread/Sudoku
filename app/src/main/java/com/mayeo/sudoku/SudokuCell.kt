package com.mayeo.sudoku

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mayeo.sudoku.attribute.isSelected

@Composable
fun SudokuCell(sudokuCellData: SudokuCellData) {
    val viewModel = LocalSudokuViewModel.current
    val color =
        animateColorAsState(targetValue = if (sudokuCellData.isSelected) MaterialTheme.colorScheme.primary.copy(alpha=0.2f) else Color.Unspecified)
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    if (isHovered) {
        viewModel.clicked(sudokuCellData)
    }

    Box(Modifier
        .fillMaxSize()
        .border(1.dp, Color.Gray)
        .background(color.value)
        .clickable { viewModel.clicked(sudokuCellData)}
    ) {
        val actualText = sudokuCellData.number?.toString() ?: ""
        sudokuCellData.attributes.forEach {
            it.Draw()
        }
        Text (
            actualText,
            modifier = Modifier.align(Alignment.Center),
            textAlign = TextAlign.Center,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize
        )
    }
}
