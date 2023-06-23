package pl.edu.agh.congard.weatherapp.backend.provider

import pl.edu.agh.congard.weatherapp.backend.ext.let
import pl.edu.agh.congard.weatherapp.backend.{GeoPlaceDetails, WeatherDetails}

import java.time.LocalDateTime
import scala.concurrent.Future
import scala.util.control.NonLocalReturns.*

trait WeatherProvider extends Nameable {
    // getters & setters in Kotlin are much more clean,
    // understandable and intuitive than accessors and mutators
    private var _place: Option[GeoPlaceDetails] = None

    def place: Option[GeoPlaceDetails] = _place  // accessor
    def place_=(place: GeoPlaceDetails): Unit = { _place = Some(place) }  // mutator

    def getResolutionHrs: Int
    def getPredictionLengthHrs: Int
    def getWeatherForecast: Seq[WeatherDetails]
    def update: Future[Unit]

    def getWeatherForNow: Option[WeatherDetails] = returning {
        for (details <- getWeatherForecast) {
            val date = details.date
            if isNow(date) then throwReturn(Some(details))
        }

        None
    }

    def getWeatherFromNow: Seq[WeatherDetails] = {
        val now = LocalDateTime.now()
        getWeatherForecast.filter(_.date.let { date =>
            date.getYear > now.getYear ||
            date.getMonth.getValue() > now.getMonth.getValue() ||
            date.getDayOfMonth > now.getDayOfMonth ||
            date.getHour >= now.getHour
        })
    }

    override def toString: String =
        f"WeatherProvider {name=$getName}"

    private def isNow(date: LocalDateTime): Boolean = LocalDateTime.now.let { now =>
        date.getYear == now.getYear &&
        date.getMonth == now.getMonth &&
        date.getDayOfMonth == now.getDayOfMonth &&
        date.getHour == now.getHour
    }
}
