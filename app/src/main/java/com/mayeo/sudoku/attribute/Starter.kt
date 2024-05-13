package com.mayeo.sudoku.attribute

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData

object Starter: SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(modifier = Modifier
            .fillMaxSize())
    }
}

val SudokuCellData.isStarter: Boolean get() = attributes.contains(Starter)