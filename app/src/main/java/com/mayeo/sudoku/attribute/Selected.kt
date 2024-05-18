package com.mayeo.sudoku.attribute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData
import com.mayeo.sudoku.ui.theme.selectedBack

object Selected : SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.selectedBack.copy(alpha = 0.1f))) {

        }
    }
}

val SudokuCellData.isSelected: Boolean get() = attributes.contains(Selected)
fun SudokuCellData.select(): SudokuCellData {
    if (attributes.contains(Editable)) {
        return copy(attributes = attributes + Selected)
    }
    return this
}

fun SudokuCellData.deselect(): SudokuCellData = copy(attributes = attributes - Selected)
