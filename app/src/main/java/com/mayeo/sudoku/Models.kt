package com.mayeo.sudoku

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import com.mayeo.sudoku.attribute.Editable
import com.mayeo.sudoku.attribute.Incorrect
import com.mayeo.sudoku.attribute.Starter
import com.mayeo.sudoku.generator.SudokuGenerator

data class SudokuTable(
    val values: List<SudokuCellData>,
) {
    fun get(row: Int, column: Int): SudokuCellData {
//        val idx = (row * FULL_HEIGHT) + column

        return values.find { it.row == row && it.column == column }!!
    }

    fun copy(row: Int, column: Int, sudokuCellData: SudokuCellData): SudokuTable {
        return values.toMutableList().apply {
            set((row * FULL_HEIGHT) + column, sudokuCellData)
        }.let {
            SudokuTable(it)
        }
    }

    fun numEmptyCells(): Int {
        return values.count {
            it.number == null
        }
    }

    fun numIncorrectCells(): Int {
        return values.count {
            it.attributes.contains(Incorrect)
        }
    }

    companion object {
        operator fun invoke(): SudokuTable {

            val sudokuGenerator = SudokuGenerator()
            val grids = sudokuGenerator.generate(cellsToKeep = TOTAL_CELLS / 4, uniqueSolution = true)!!
            val (sudokuGrid, fullGrid) = grids

            fullGrid.forEach { row ->
                println(row.joinToString(" "))
            }

            return SudokuTable(buildList {
                sudokuGrid.forEachIndexed { row, rowData ->
                    rowData.forEachIndexed { column, number ->
                        val fullNumber = fullGrid[row][column]
                        add(SudokuCellData(
                            if (number > 0) number else null,
                            fullNumber,
                            row,
                            column,
                            if (number != 0) setOf(Starter) else setOf(Editable),
                        ))
                    }
                }
            })
        }
    }
}

data class SudokuCellData(
    val number: Int?,
    val actualNumber: Int,
    val row: Int,
    val column: Int,
    val attributes: Set<Attribute> = setOf(),
){
    interface Attribute {
        @Composable
        fun Draw()
    }
}

fun SudokuCellData.clearActualValue(): SudokuCellData {
    return copy(number = null)
}

enum class NumberInputType {
    Actual,
    Note
}

internal val LocalSudokuViewModel = compositionLocalOf { SudokuViewModel(SudokuTable()) }