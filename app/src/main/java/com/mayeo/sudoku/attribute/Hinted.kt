package com.mayeo.sudoku.attribute

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mayeo.sudoku.SudokuCellData
import com.mayeo.sudoku.ui.theme.hintedBack

object Hinted: SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.hintedBack.copy(alpha = 0.3f)))
    }
}

fun SudokuCellData.setHinted(): SudokuCellData = copy(attributes = attributes + Hinted)