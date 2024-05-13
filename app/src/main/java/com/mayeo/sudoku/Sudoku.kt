package com.mayeo.sudoku

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mayeo.sudoku.generator.SudokuGenerator

@Composable
fun SudokuView() {
    // TODO
    //  - Cell selection method?
    //  -- Get rid of multiselect
    //  - Highlight 3x3 block, plus row & column?
    //  - Hints, cell checker, toast to pop up message to see?
    //  - Other types of sudoku (killer, etc.)
    //  - Time & score?
    //  - Deselect when actual number entered (regardless of correctness)
    //  - Catch for solutions that can't be worked out (ANR in logcat...)
    //  - splash screen/theme when generating puzzle
    //  - Difficulty selection (and figuring out difficulty levels)

    // Done
    //  - Extra layer to array for sudoku grid? What's causing that?
    //  - Different-sized grids (square, rectangular [2x2, 4x4, 4x3 etc.]
    //  - Number type for "default" vs. "written/solved" numbers
    //  - Clear button doesn't work - clear selection (selected cells), separate delete button?
    //  - "Delete" button, to clear numbers from cell (all selected, notes only? separate for "actual" and "notes")
    //    - Takes number input mode into account
    //  - Filling "Actual" number should clear notes from cell
    //  - implement alg to generate solved table, plus initial squares

//    val initialTable = SudokuTable(
//        Pair(2, 3) to 4,
//        Pair(8, 9) to 5
//    )

    val initialTable = SudokuTable()
    println(initialTable.values.size)

    val viewModel = remember {
        SudokuViewModel(initialTable)
    }

    CompositionLocalProvider(
        LocalSudokuViewModel provides viewModel
    ) {
        Column(
            Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    viewModel.clear()
                }
                .padding(32.dp)
        ){
            SudokuTableView(
                Modifier
                    .weight(2.5f)
                    .align(Alignment.CenterHorizontally)
            )

            SudokuControls(
                numberInputType = viewModel.numberInputType().collectAsState().value,
                Modifier
                    .weight(1f)
                    .align(Alignment.CenterHorizontally)
            ) {
                viewModel.changeNumberType(it)
            }
        }
    }
}

@Composable
private fun SudokuTableView(modifier: Modifier = Modifier) {
    // Creates the full sudoku table, collection of grids (SudokuBox), which contain the
    //  individual cells (SudokuCell)
    val viewModel = LocalSudokuViewModel.current
    val sudokuTable = viewModel.table().collectAsState().value
    println(sudokuTable.values)
    BoxWithConstraints(
        modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        val width = minOf(maxWidth, maxHeight) / FULL_WIDTH
        Row {
            repeat(SUBGRID_ROWS) { boxCol ->
                Column {
                    repeat(SUBGRID_COLS) { boxRow ->
                        Box(
                            Modifier
                                .width(width * MAINGRID_ROWS)
                                .height(width * MAINGRID_COLS)
                        ) {
                            SudokuBox(boxRow, boxCol)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SudokuBox(boxRow: Int, boxCol: Int) {
    // Creates a subgrid of "x" cells depending on subgrid row/column properties
    val sudokuTable = LocalSudokuViewModel.current.table().collectAsState().value
    BoxWithConstraints(
        Modifier
            .fillMaxSize()
            .border(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        val width = maxWidth / MAINGRID_ROWS
        val height = maxHeight / MAINGRID_COLS

        Row {
            repeat(SUBGRID_COLS) { repeatedColumn ->
                Column {
                    repeat(SUBGRID_ROWS) { repeatedRow ->
                        val column = (boxCol * SUBGRID_COLS) + repeatedColumn
                        val row = (boxRow * SUBGRID_ROWS) + repeatedRow
                        val sudokuCellValues = sudokuTable.get(row, column)
                        Box(Modifier.size(width, height)) {
                            SudokuCell(sudokuCellData = sudokuCellValues)
                        }
                    }
                }
            }
        }
    }
}

@Preview(widthDp = 500, heightDp = 500)
@Composable
fun SudokuPreview(){
    SudokuView()
}