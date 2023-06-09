package pl.edu.agh.congard.weatherapp.math

import java.lang.Math.abs
import scala.util.control.NonLocalReturns.*

def almostEquals(v1: Seq[Double], v2: Seq[Double], tolerance: Double = 10E-8): Boolean = returning {
    if (v1.length != v2.length)
        throwReturn(false)
    
    for (i <- v1.indices) {
        if (abs(v1(i) - v2(i)) > tolerance) {
            throwReturn(false)
        }
    }

    true
}

def almostEquals(v1: Array[Double], v2: Array[Double]): Boolean =
    almostEquals(v1.toIndexedSeq, v2.toIndexedSeq)

def almostEquals(m1: Matrix, m2: Matrix, tolerance: Double): Boolean = returning {
    if (m1.length != m2.length)
        throwReturn(false)
    
    for (i <- m1.indices) {
        if (!almostEquals(m1(i).toIndexedSeq, m2(i).toIndexedSeq, tolerance)) {
            throwReturn(false)
        }
    }

    true
}

def almostEquals(m1: Matrix, m2: Matrix): Boolean = almostEquals(m1, m2, 10E-8)
