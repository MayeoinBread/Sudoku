package com.mayeo.sudoku

import android.widget.Toast
import com.mayeo.sudoku.attribute.clearNoteValues
import com.mayeo.sudoku.attribute.deselect
import com.mayeo.sudoku.attribute.isEditable
import com.mayeo.sudoku.attribute.isIncorrect
import com.mayeo.sudoku.attribute.isSelected
import com.mayeo.sudoku.attribute.removeEditable
import com.mayeo.sudoku.attribute.removeIncorrect
import com.mayeo.sudoku.attribute.select
import com.mayeo.sudoku.attribute.setCorrect
import com.mayeo.sudoku.attribute.setHinted
import com.mayeo.sudoku.attribute.setIncorrect
import com.mayeo.sudoku.attribute.toggleNoteValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SudokuViewModel(sudokuTable: SudokuTable) {
    private val _table = MutableStateFlow(sudokuTable)
    private val _numberInputType = MutableStateFlow(NumberInputType.Actual)
    private var _lastSelected: SudokuCellData? = null

    fun table(): StateFlow<SudokuTable> = _table
    fun numberInputType(): StateFlow<NumberInputType> = _numberInputType

    var numEmptyCells = MutableStateFlow(_table.value.numEmptyCells())

    private fun updateCell(
        sudokuCellData: SudokuCellData,
        newSudokuCellData: SudokuCellData
    ) {
        val state = _table.value
        val newState =
            state.copy(sudokuCellData.row, sudokuCellData.column, newSudokuCellData)
        _table.value = newState

        numEmptyCells.value = newState.numEmptyCells()
    }

    fun clicked(sudokuCellData: SudokuCellData) {
        // Handling for single-cell selection
        _lastSelected = if (!sudokuCellData.isSelected) {
            updateCell(sudokuCellData, sudokuCellData.select())
            if (_lastSelected != null) {
                // Get a new reference in case the cell has been updated
                val lastCell = _table.value.get(_lastSelected!!.row, _lastSelected!!.column)
                updateCell(lastCell, lastCell.deselect())
            }
            sudokuCellData
        } else {
            updateCell(sudokuCellData, sudokuCellData.deselect())
            null
        }
    }

    fun onNumberPressed(number: Int) {
        _table.value.values.map {
            if (it.isSelected && it.isEditable) {
                val newCell = when(_numberInputType.value) {
                    NumberInputType.Actual -> {
                        it.clearNoteValues().copy(number = number)
                    }
                    NumberInputType.Note -> {
                        it.clearActualValue().toggleNoteValue(number)
                    }
                }
                if (it.isIncorrect) {
                    updateCell(it, newCell.removeIncorrect())
                } else {
                    updateCell(it, newCell)
                }
            }
        }
    }

    fun changeNumberType(numberInputType: NumberInputType) {
        _numberInputType.value = numberInputType
    }

    fun clear() {
        _table.value.values.map {
            if (it.isSelected) {
                updateCell(it, it.deselect())
            }
        }
    }

    fun delete(numberType: NumberInputType = _numberInputType.value) {
        _table.value.values.map {
            if (it.isSelected && it.isEditable) {
                val newCell = when(numberType) {
                    NumberInputType.Actual -> {
                        it.copy(number = null)
                    }
                    NumberInputType.Note -> {
                        it.clearNoteValues()
                    }
                }
                if (it.isIncorrect){
                    updateCell(it, newCell.removeIncorrect())
                } else {
                    updateCell(it, newCell)
                }
            }
        }
    }

    fun checkCell() {
        // Check if selected cell(s) is/are correct
        // If so, mark as Correct and not Editable
        // If not, mark as Incorrect
        // TODO handling for NoteValue cell? or will null check below handle that?
        _table.value.values.map {
            if (it.isSelected && it.isEditable) {
                if (it.number != null) {
                    println("Cell: {${it.number}} Actual: {${it.actualNumber}}")
                    val newCell = if (it.number == it.actualNumber) {
                        println("Correct!")
                        it.setCorrect().removeEditable().deselect()
                    } else {
                        println("Incorrect...")
                        it.setIncorrect()
                    }
                    updateCell(it, newCell)
                }
            }
        }
    }

    fun giveHint() {
        // Solve the selected cell(s)
        // Mark cell as Hinted and not Editable
        _table.value.values.map {
            if(it.isSelected && it.isEditable) {
                val newCell = it.clearNoteValues().copy(number = it.actualNumber).setHinted().deselect().removeEditable()
                updateCell(it, newCell)
            }
        }
    }

    fun solvePuzzle(): Int {
        _table.value.values.map {
            if (it.isEditable) {
                val newCell =
                if (it.number == null || it.number == it.actualNumber){  // Hacky catch for dev purposes
                    it.copy(number = it.actualNumber).setCorrect().removeEditable().deselect()
                } else {
                    it.setIncorrect()
                }
                updateCell(it, newCell)
            }
        }

        return _table.value.numIncorrectCells()
    }
}