package com.mayeo.sudoku.attribute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData
import com.mayeo.sudoku.ui.theme.incorrectBack

object Incorrect: SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.incorrectBack.copy(alpha = 0.3f)))
    }
}

val SudokuCellData.isIncorrect: Boolean get() = attributes.contains(Incorrect)
fun SudokuCellData.setIncorrect(): SudokuCellData = copy(attributes = attributes + Incorrect)

fun SudokuCellData.removeIncorrect(): SudokuCellData = copy(attributes = attributes - Incorrect)