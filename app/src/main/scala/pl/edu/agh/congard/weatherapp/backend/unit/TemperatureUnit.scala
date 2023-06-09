package pl.edu.agh.congard.weatherapp.backend.unit

enum TemperatureUnit(t: Double, name: String) extends MeasureUnit(t, name) {
    case Celsius(c: Double) extends TemperatureUnit(c, TemperatureUnit.celsiusStr)
    case Fahrenheit(val f: Double) extends TemperatureUnit(f, TemperatureUnit.fahrenheitStr)
}

object TemperatureUnit {
    private final val celsiusStr = "°C"
    private final val fahrenheitStr = "°F"

    def byName(t: Double, name: String): TemperatureUnit = name match
        case TemperatureUnit.celsiusStr => Celsius(t)
        case TemperatureUnit.fahrenheitStr => Fahrenheit(t)
        case _ => throw IllegalArgumentException(s"Unknown temperature unit: $name")
}
