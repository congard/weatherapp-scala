package pl.edu.agh.congard.weatherapp.backend.unit

class PercentUnit(p: Double) extends MeasureUnit(p, "%")

object PercentUnit {
    def apply(p: Double): PercentUnit =
        new PercentUnit(p)
}
