package com.mayeo.sudoku.attribute

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.mayeo.sudoku.SudokuCellData

data class NoteValue(val values: Set<Int>): SudokuCellData.Attribute {
    @Composable
    override fun Draw() {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                values.sorted().joinToString(""),
                modifier = Modifier.align(Alignment.Center),
                textAlign = TextAlign.Center,
                fontSize = TextUnit(9f, TextUnitType.Sp)
            )
        }
    }
}

fun SudokuCellData.toggleNoteValue(value: Int): SudokuCellData {
    val currentValues =
        attributes.filterIsInstance<NoteValue>().firstOrNull()?.values.orEmpty().let {
            if (it.contains(value)) {
                it - value
            } else {
                it + value
            }
        }
    return removeNoteValues().let{
        it.copy(attributes = it.attributes + NoteValue(currentValues.toSet()))
    }
}

fun SudokuCellData.removeNoteValues(): SudokuCellData {
    return copy(attributes = attributes.filterNot { it is NoteValue}.toSet())
}

fun SudokuCellData.clearNoteValues(): SudokuCellData {
//    println(attributes)
//    println(attributes.filterNot { it is NoteValue }.toSet())
    return copy(attributes = attributes.filterNot { it is NoteValue }.toSet())
}