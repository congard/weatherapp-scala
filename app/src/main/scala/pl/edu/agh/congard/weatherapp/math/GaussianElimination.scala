package pl.edu.agh.congard.weatherapp.math

import scala.math.abs

/**
  * Algorithm for solving systems of linear equations
  * with improved numerical stability
  */
object GaussianElimination {
    def eliminate(matrix: Matrix): Unit = {
        val rows = matrix.length

        for (i <- 0 until rows - 1) {
            var pivotVal = matrix(i)(i)
            var pivotRow = i

            for (j <- i + 1 until rows) {
                val value = matrix(j)(i)

                // we should find not only non-zero pivot, but also
                // the largest possible absolute value of it. It will
                // greatly improve numerical stability.
                if (abs(value) > abs(pivotVal)) {
                    pivotVal = value
                    pivotRow = j
                }
            }

            // swap rows
            val tmpRow = matrix(i)
            matrix(i) = matrix(pivotRow)
            matrix(pivotRow) = tmpRow

            val row_i = matrix(i)

            for (j <- i + 1 until rows) {
                val row_j = matrix(j)
                val mul = matrix(j)(i) / pivotVal

                for (k <- i until row_j.length) {
                    row_j(k) -= row_i(k) * mul
                }
            }
        }
    }

    def substitute(matrix: Matrix): Vec = {
        val n = matrix.length
        val result = new Vec(n)

        for (i <- n - 1 to 0 by -1) {
            val row = matrix(i)
            var b = row(row.length - 1)

            for (j <- i + 1 until n)
                b -= row(j) * result(j)

            result(i) = b / row(i)
        }

        result
    }

    def solve(matrix: Matrix): Vec = {
        eliminate(matrix)
        substitute(matrix)
    }
}
