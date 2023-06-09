package pl.edu.agh.congard.weatherapp.math

import org.scalatest.*
import org.scalatest.funsuite.AnyFunSuiteLike
import org.scalatest.matchers.*

class GaussianEliminationTest extends AnyFunSuiteLike {
    // def to prevent from modifying the original

    private def solution = Array(-5.0, 5.0, -1.0)

    private def matrix = Array(
        Array(1.0, 3.0, 1.0, 9.0),
        Array(1.0, 1.0, -2.0, 2.0),
        Array(3.0, 11.0, 5.0, 35.0))

    private def eliminated = Array(
        Array(3.0, 11.0, 5.0, 35.0),
        Array(0.0, -2.0 - 2.0 / 3.0, -3.0 - 2.0 / 3.0, -9.0 - 2.0 / 3.0),
        Array(0.0, 0.0, 0.25, -0.25))

    test("testSolve") {
        val expected = solution
        val actual = GaussianElimination.solve(matrix)
        assert(almostEquals(expected, actual))
    }

    test("testEliminate") {
        val expected = eliminated
        val actual = matrix

        GaussianElimination.eliminate(actual)

        assert(almostEquals(expected, actual))
    }

    test("testSubstitute") {
        val expected = solution
        val actual = GaussianElimination.substitute(eliminated)
        assert(almostEquals(expected, actual))
    }
}
