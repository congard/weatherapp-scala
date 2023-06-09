package pl.edu.agh.congard.weatherapp.backend.unit

import scala.math.{abs, signum}

abstract class MeasureUnit(val value: Double, val name: String) {
    override def toString: String =
        f"$value$name"

    def compare(that: MeasureUnit): Int = {
        assert(name == that.name)
        val delta = value - that.value
        if (abs(delta) <= 10E-8) 0
        else signum(delta).toInt
    }
}
