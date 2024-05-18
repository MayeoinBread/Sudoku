package com.mayeo.sudoku.attribute

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData

object Editable: SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(modifier = Modifier
            .fillMaxSize())
    }
}

val SudokuCellData.isEditable: Boolean get() = attributes.contains(Editable)

fun SudokuCellData.setEditable(): SudokuCellData = copy(attributes = attributes + Editable)

fun SudokuCellData.removeEditable(): SudokuCellData = copy(attributes = attributes - Editable)