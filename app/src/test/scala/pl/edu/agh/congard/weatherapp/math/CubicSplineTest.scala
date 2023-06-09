package pl.edu.agh.congard.weatherapp.math

import org.scalatest.funsuite.AnyFunSuiteLike

import scala.collection.immutable.ArraySeq

class CubicSplineTest extends AnyFunSuiteLike {
    private val xsNodes = ArraySeq[Double](0, 1, 2, 3, 4)
    private val ysNodes = ArraySeq[Double](21, 24, 24, 18, 16)

    private val xs = ArraySeq[Double](
        0.0, 0.16666666666666666, 0.3333333333333333, 0.5, 0.6666666666666666,
        0.8333333333333333, 1.0, 1.1666666666666665, 1.3333333333333333, 1.5,
        1.6666666666666665, 1.8333333333333333, 2.0, 2.1666666666666665, 2.333333333333333,
        2.5, 2.6666666666666665, 2.833333333333333, 3.0, 3.1666666666666665,
        3.333333333333333, 3.5, 3.6666666666666665, 3.833333333333333, 4.0)

    test("testApplyNaturalBoundary") {
        val spline = CubicSpline(xsNodes, ysNodes)(CubicSpline.Boundary.Natural)

        val ysExpected = ArraySeq[Double](
            21.0, 21.549189814814813, 22.08994708994709, 22.613839285714285, 23.11243386243386,
            23.577298280423275, 23.999999999999993, 24.366650132275122, 24.641534391534382, 24.78348214285713,
            24.75132275132274, 24.503885582010568, 23.999999999999986, 23.220320767195773, 22.23280423280424,
            21.127232142857153, 19.993386243386254, 18.921048280423292, 18.000000000000014, 17.298363095238116,
            16.79761904761905, 16.457589285714292, 16.238095238095227, 16.09895833333333, 16.000000000000014)
        
        val ysActual = for x <- xs yield spline(x)

        assert(almostEquals(ysExpected, ysActual))
    }

    test("testApplyQuadraticBoundary") {
        val spline = CubicSpline(xsNodes, ysNodes)(CubicSpline.Boundary.Quadratic)

        val ysExpected = ArraySeq[Double](
            21.000000000000007, 21.606481481481488, 22.170370370370375, 22.69166666666667, 23.17037037037037,
            23.60648148148148, 24.0, 24.34413580246912, 24.604938271604922, 24.74166666666665,
            24.713580246913562, 24.47993827160492, 23.99999999999998, 23.253086419753068, 22.298765432098755,
            21.216666666666647, 20.086419753086396, 18.987654320987637, 17.999999999999993, 17.189814814814824,
            16.570370370370377, 16.141666666666673, 15.903703703703705, 15.856481481481488, 16.0)
        
        val ysActual = for x <- xs yield spline(x)

        assert(almostEquals(ysExpected, ysActual))
    }

    test("testApplyNotAKnotBoundary") {
        val spline = CubicSpline(xsNodes, ysNodes)(CubicSpline.Boundary.NotAKnot)

        val ysExpected = ArraySeq[Double](
            21.000000000000004, 21.443094135802472, 21.94753086419753, 22.484375, 23.024691358024693,
            23.53954475308642, 24.0, 24.37712191358024, 24.641975308641968, 24.765624999999993,
            24.71913580246913, 24.47357253086419, 23.99999999999999, 23.284529320987666, 22.373456790123477,
            21.328125000000007, 20.209876543209866, 19.08005401234565, 17.999999999999993, 17.031057098765412,
            16.234567901234538, 15.67187500000001, 15.404320987654327, 15.493248456790067, 15.999999999999996)
        
        val ysActual = for x <- xs yield spline(x)

        assert(almostEquals(ysExpected, ysActual))
    }
}
