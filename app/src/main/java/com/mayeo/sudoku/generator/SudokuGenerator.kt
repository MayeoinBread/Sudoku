package com.mayeo.sudoku.generator

import com.mayeo.sudoku.FULL_HEIGHT
import com.mayeo.sudoku.FULL_WIDTH
import com.mayeo.sudoku.MAINGRID_COLS
import com.mayeo.sudoku.MAINGRID_ROWS
import com.mayeo.sudoku.MAX_VAL
import com.mayeo.sudoku.SUBGRID_COLS
import com.mayeo.sudoku.SUBGRID_ROWS
import com.mayeo.sudoku.TOTAL_CELLS
import com.mayeo.sudoku.isValid
import kotlin.random.Random

class SudokuGenerator {
    private var cellsToKeep: Int = TOTAL_CELLS

    private lateinit var grid: Array<IntArray>
    private lateinit var fullGrid: Array<IntArray>

    fun generate(cellsToKeep: Int, uniqueSolution: Boolean = false): Pair<Array<IntArray>, Array<IntArray>>? {
        this.cellsToKeep = cellsToKeep

        grid = Array(FULL_HEIGHT) { IntArray(FULL_WIDTH)}
        fillGrid()

        if (uniqueSolution && !hasUniqueSolution()) {
            return null
        }
        return Pair(grid, fullGrid)
    }

    private fun fillGrid() {
        fillDiagonal()

        if(fillRemaining(0, 0)) {
            removeNumbers()
        } else {
            fillGrid()
        }
    }

    private fun fillDiagonal() {
        for (i in 0 until FULL_HEIGHT step SUBGRID_ROWS) {
            fillSubGrid(i, i)
        }
    }

    private fun fillSubGrid(row: Int, col: Int) {
        val numList = (1..MAX_VAL).toMutableList().shuffled()
        var numIndex = 0
        val used = mutableSetOf<Int>()
        for (i in 0 until SUBGRID_ROWS) {
            for (j in 0 until SUBGRID_COLS) {
                val rowIndex = row + i
                val colIndex = col + j
                if (rowIndex < FULL_HEIGHT && colIndex < FULL_WIDTH) {
                    while (numList[numIndex] in used) {
                        numIndex = (numIndex + 1) % MAX_VAL
                    }
                    grid[rowIndex][colIndex] = numList[numIndex]
                    used.add(numList[numIndex])
                    numIndex = (numIndex + 1) % MAX_VAL
                }
            }
        }
    }

    private fun fillRemaining(row: Int, col: Int): Boolean {
        var currRow = row
        var currCol = col

        if (col == FULL_WIDTH) {
            currRow++
            currCol = 0
            if (currRow == FULL_HEIGHT) {
                return true
            }
        }

        if (grid[currRow][currCol] != 0) {
            return fillRemaining(currRow, currCol + 1)
        }

        for (num in 1..MAX_VAL) {
            if (isValid(grid, currRow, currCol, num)) {
                grid[currRow][currCol] = num
                if (fillRemaining(currRow, currCol + 1)) {
                    return true
                }
                grid[currRow][currCol] = 0
            }
        }
        return false
    }

    private fun removeNumbers() {
        val totalCells = TOTAL_CELLS
        var remainingCells = totalCells

        fullGrid = grid.map {it.copyOf()}.toTypedArray()

        while (remainingCells > cellsToKeep) {
            println("iteration:")
            grid.forEach { row ->
                println(row.joinToString(" "))
            }
            println(" ")
            val row = Random.nextInt(MAX_VAL)
            val col = Random.nextInt(MAX_VAL)
            if (grid[row][col] != 0) {
                val temp = grid[row][col]
                grid[row][col] = 0

                if (!hasUniqueSolution()) {
                    grid[row][col] = temp
                }
                remainingCells--
            }
        }
    }

    private fun hasUniqueSolution(): Boolean {
        val tGrid = grid.map {it.copyOf()}.toTypedArray()
        val solver = SudokuSolver(tGrid)
        val hasSolution = solver.solve() && !solver.hasMultipleSolutions()
        println("hasSolution: {$hasSolution}")
        return hasSolution
    }
}

class SudokuSolver(private val grid: Array<IntArray>) {
    private val size = grid.size

    fun solve(): Boolean {
        val emptyCell = findEmptyCell() ?: return true

        val (row, col) = emptyCell
        for(num in 1..size) {
            if (isValid(grid, row, col, num)) {
                grid[row][col] = num
                if (solve()) {
                    return true
                }
                grid[row][col] = 0
            }
        }
        return false
    }

    fun hasMultipleSolutions(): Boolean {
        val solver = SudokuSolver(grid)
        val originalGrid = grid.map {it.copyOf()}.toTypedArray()
        solver.solve()
        val solution1 = solver.grid
        grid.forEachIndexed { rowIndex, row ->
            row.forEachIndexed {colIndex, _ ->
                grid[rowIndex][colIndex] = originalGrid[rowIndex][colIndex]
            }
        }
        solver.solve()
        val solution2 = solver.grid
        return !solution1.contentDeepEquals(solution2)
    }

    private fun findEmptyCell(): Pair<Int, Int>? {
        for (row in 0 until size) {
            for (col in 0 until size) {
                if (grid[row][col] == 0) {
                    return Pair(row, col)
                }
            }
        }
        return null
    }
}