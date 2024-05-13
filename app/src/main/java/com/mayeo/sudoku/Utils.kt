package com.mayeo.sudoku

const val SUBGRID_ROWS = 3  // 2
const val SUBGRID_COLS = 3
const val MAINGRID_ROWS = 3
const val MAINGRID_COLS = 3  // 2
const val MIN_VAL = 1
const val MAX_VAL = SUBGRID_ROWS * SUBGRID_COLS
const val FULL_WIDTH = MAINGRID_COLS * SUBGRID_COLS
const val FULL_HEIGHT = MAINGRID_ROWS * SUBGRID_ROWS
const val TOTAL_CELLS = FULL_WIDTH * FULL_HEIGHT


fun isValid(grid: Array<IntArray>, row: Int, col: Int, num: Int): Boolean {
    return !isInRow(grid, row, num)
            && !isInCol(grid, col, num)
            && !isInSubGrid(grid, row - row % SUBGRID_ROWS, col - col % SUBGRID_COLS, num)
}

fun isInRow(grid: Array<IntArray>, row: Int, num: Int): Boolean {
    return grid[row].any { it == num }
}

fun isInCol(grid: Array<IntArray>, col: Int, num: Int): Boolean {
    return grid.any { it[col] == num }
}

private fun isInSubGrid(grid: Array<IntArray>, startRow: Int, startCol: Int, num: Int): Boolean {
//    println("startRow: {$startRow}, startCol: {$startCol}")
    for (i in 0 until SUBGRID_ROWS) {
        for (j in 0 until SUBGRID_COLS) {
//            println("InSubGrid: i: {$i}, j: {$j}")
            if (grid[startRow + i][startCol + j] == num) {
                return true
            }
        }
    }
    return false
}