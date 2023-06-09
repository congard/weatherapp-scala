package pl.edu.agh.congard.weatherapp.backend.provider

import pl.edu.agh.congard.weatherapp.backend.WeatherDetails
import pl.edu.agh.congard.weatherapp.backend.ext.ResponseExtensions
import pl.edu.agh.congard.weatherapp.backend.unit.{PercentUnit, PrecipitationUnit, TemperatureUnit}
import requests.Response

import java.time.format.DateTimeFormatter
import java.time.{Instant, LocalDateTime, OffsetDateTime}
import java.util
import java.util.Date
import scala.collection.mutable.ListBuffer
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.jdk.CollectionConverters.*

class OpenMeteoWeatherProvider extends WeatherProvider {
    private val weatherForecast = ListBuffer[WeatherDetails]()

    override def getName: String = "OpenMeteo"

    override def getResolutionHrs: Int = 1

    override def getPredictionLengthHrs: Int =
        weatherForecast.length

    override def getWeatherForecast: Seq[WeatherDetails] =
        weatherForecast.toList

    override def update: Future[Unit] = Future {
        if place.isEmpty then
            Future.failed(IllegalStateException("Place is not set"))

        val placeDetails = place.get

        val r: Response = requests.get("https://api.open-meteo.com/v1/forecast",
            params = Map(
                "latitude" -> placeDetails.lat.toString,
                "longitude" -> placeDetails.lon.toString,
                "hourly" -> "temperature_2m,relativehumidity_2m,apparent_temperature,precipitation_probability,precipitation,rain,showers,snowfall",
                "timezone" -> "auto"
            ))

        if !r.isSuccessful then
            Future.failed(IllegalStateException(s"Illegal status code: ${r.statusCode}"))

        val respObj = r.asJSONObject

        // parse units
        val unitsObj = respObj("hourly_units")

        val tempUnits = unitsObj("temperature_2m").str
        val apparentTempUnits = unitsObj("apparent_temperature").str
        val relHumidityUnits = unitsObj("relativehumidity_2m").str
        val precipitationProbUnits = unitsObj("precipitation_probability").str
        val precipitationUnits = unitsObj("precipitation").str
        val rainUnits = unitsObj("rain").str
        val showersUnits = unitsObj("showers").str
        val snowfallUnits = unitsObj("snowfall").str

        // construct weather details
        val hourlyObj = respObj("hourly")
        val timeArray = hourlyObj("time")
        val tempArray = hourlyObj("temperature_2m")
        val apparentTempArray = hourlyObj("apparent_temperature")
        val relHumidityArray = hourlyObj("relativehumidity_2m")
        val precipitationProbArray = hourlyObj("precipitation_probability")
        val precipitationArray = hourlyObj("precipitation")
        val rainArray = hourlyObj("rain")
        val showersArray = hourlyObj("showers")
        val snowfallArray = hourlyObj("snowfall")

        weatherForecast.clear()

        for (i <- timeArray.arr.indices) {
            val timeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            val localDateTime = LocalDateTime.parse(timeArray(i).str, timeFormatter)

            weatherForecast.addOne(WeatherDetails(
                date = localDateTime,
                temp = TemperatureUnit.byName(tempArray(i).num, tempUnits),
                apparentTemp = TemperatureUnit.byName(apparentTempArray(i).num, tempUnits),
                humidity = PercentUnit(relHumidityArray(i).num),
                precipitationProbability = PercentUnit(precipitationProbArray(i).num),
                precipitation = PrecipitationUnit.byName(precipitationArray(i).num, precipitationUnits),
                rain = PrecipitationUnit.byName(rainArray(i).num, rainUnits),
                showers = PrecipitationUnit.byName(showersArray(i).num, showersUnits),
                snowfall = PrecipitationUnit.byName(snowfallArray(i).num, snowfallUnits)
            ))
        }
    }
}
