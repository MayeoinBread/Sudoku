package com.mayeo.sudoku.attribute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData

object Selected : SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))) {

        }
    }
}

val SudokuCellData.isSelected: Boolean get() = attributes.contains(Selected)
fun SudokuCellData.select(): SudokuCellData = copy(attributes = attributes + Selected)
fun SudokuCellData.deselect(): SudokuCellData = copy(attributes = attributes - Selected)
