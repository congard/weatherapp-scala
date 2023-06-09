package pl.edu.agh.congard.weatherapp.backend

import pl.edu.agh.congard.weatherapp.backend.unit.{PercentUnit, PrecipitationUnit, TemperatureUnit}

import java.time.LocalDateTime

class WeatherDetails(
    val date: LocalDateTime,
    val temp: TemperatureUnit,
    val apparentTemp: TemperatureUnit,
    val humidity: PercentUnit,
    val precipitationProbability: PercentUnit,
    val precipitation: PrecipitationUnit,
    val rain: PrecipitationUnit,
    val showers: PrecipitationUnit,
    val snowfall: PrecipitationUnit
) {
    override def toString: String =
        s"WeatherDetails(date=$date, " +
            s"temp=$temp, " +
            s"apparentTemp=$apparentTemp, " +
            s"humidity=$humidity, " +
            s"precipitationProbability=$precipitationProbability, " +
            s"precipitation=$precipitation, " +
            s"rain=$rain, " +
            s"showers=$showers, " +
            s"snowfall=$snowfall)"
}
