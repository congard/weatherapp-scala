package pl.edu.agh.congard.weatherapp.math

import scala.annotation.tailrec
import scala.math.pow

// s_i(x)   = a_i * x^3 + b_i * x^2 + c_i * x + d_i
// s_i'(x)  = 3 * a_i * x^2 + 2 * b_i * x + c_i
// s_i''(x) = 6 * a_i * x + 2 * b_i

// cubic spline:
// s_i(x_i) = f(x_i)                     (1)
// s_i(x_{i+1}) = f(x_{i+1})             (1)
// s_i'(x_{i+1}) = s_{i+1}'(x_{i+1})     (2)
// s_i''(x_{i+1}) = s_{i+1}''(x_{i+1})   (3)

/**
  * Basic implementation of Cubic Spline
  */
object CubicSpline {
    enum Boundary {
        case Natural
        case Quadratic
        case NotAKnot
    }

    def apply(xs: Seq[Double], ys: Seq[Double])(implicit boundary: Boundary = Boundary.Natural): Double => Double = {
        assert(xs.length == ys.length)

        val n = xs.length
        val coefficients = calcCoefficients(xs, ys)(boundary)

        x => {
            // https://stackoverflow.com/questions/2742719/how-do-i-break-out-of-a-loop-in-scala
            
            @tailrec
            def findIntervalIndex(i: Int = 1): Int = {
                if (i == n)
                    return -1
                if (x <= xs(i)) {
                    i - 1
                } else {
                    findIntervalIndex(i + 1)
                }
            }

            val intervalIndex = findIntervalIndex()

            val a = coefficients(4 * intervalIndex + 0)
            val b = coefficients(4 * intervalIndex + 1)
            val c = coefficients(4 * intervalIndex + 2)
            val d = coefficients(4 * intervalIndex + 3)

            a * pow(x, 3) + b * pow(x, 2) + c * x + d
        }
    }

    private def calcCoefficients(xs: Seq[Double], ys: Seq[Double])(boundary: Boundary = Boundary.Natural): Vec = {
        val n = xs.length
        val rows = 4 * (n - 1)
        val cols = 4 * (n - 1) + 1 // for B

        val matrix = new Matrix(rows)
        var _matrixIndex = 0

        def matrixAppend(v: Vec): Unit = {
            matrix(_matrixIndex) = v
            _matrixIndex += 1
        }

        // condition (1):
        //   s_i(x_i) = f(x_i),
        //   s_i(x_{i+1}) = f(x_{i+1})
        // = 2 * (n - 1) equations

        for (i <- 0 until n - 1) {
            def eq_cond1(j: Int, eq: Vec): Unit = {
                eq(4 * i + 0) = pow(xs(j), 3)  // a_j
                eq(4 * i + 1) = pow(xs(j), 2)  // b_j
                eq(4 * i + 2) = xs(j)  // c_j
                eq(4 * i + 3) = 1  // d_j
                eq(eq.length - 1) = ys(j)  // B
            }

            val eq1 = new Vec(cols)
            val eq2 = new Vec(cols)

            eq_cond1(i, eq1)
            eq_cond1(i + 1, eq2)

            matrixAppend(eq1)
            matrixAppend(eq2)
        }

        // condition (2):
        //   s_i'(x_{i+1}) = s_{i+1}'(x_{i+1})

        for (i <- 0 until n - 2) {
            // side: 1 means left, -1 means right
            def eq_cond2(j: Int, eq: Vec, side: Int): Unit = {
                eq(4 * j + 0) = 3 * pow(xs(i + 1), 2) * side    // a_j
                eq(4 * j + 1) = 2 * xs(i + 1) * side            // b_j
                eq(4 * j + 2) = 1 * side                        // c_j
            }

            val eq1 = new Vec(cols)
            eq_cond2(i, eq1, 1)
            eq_cond2(i + 1, eq1, -1)
            matrixAppend(eq1)
        }

        // condition (3):
        //   s_i''(x_{i+1}) = s_{i+1}''(x_{i+1})

        for (i <- 0 until n - 2) {
            // side: 1 means left, -1 means right
            def eq_cond3(j: Int, eq: Vec, side: Int): Unit = {
                eq(4 * j + 0) = 6 * xs(i + 1) * side    // a_j
                eq(4 * j + 1) = 2 * side                // b_j
            }

            val eq1 = new Vec(cols)
            eq_cond3(i, eq1, 1)
            eq_cond3(i + 1, eq1, -1)
            matrixAppend(eq1)
        }

        val eq1 = new Vec(cols)
        val eq2 = new Vec(cols)

        boundary match {
            case Boundary.Natural =>
                eq1(4 * 0 + 0) = 6 * xs.head
                eq1(4 * 0 + 1) = 2
                eq2(4 * (n - 2) + 0) = 6 * xs.last
                eq2(4 * (n - 2) + 1) = 2
            case Boundary.Quadratic =>
                eq1(4 * 0 + 0) = 1
                eq2(4 * (n - 2) + 0) = 1
            case Boundary.NotAKnot =>
                eq1(4 * 0 + 0) = 1
                eq1(4 * 1 + 0) = -1
                eq2(4 * (n - 3) + 0) = 1
                eq2(4 * (n - 2) + 0) = -1
        }

        matrixAppend(eq1)
        matrixAppend(eq2)

        GaussianElimination.solve(matrix)
    }
}
