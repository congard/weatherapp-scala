package pl.edu.agh.congard.weatherapp

import javafx.application.Application
import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails
import pl.edu.agh.congard.weatherapp.backend.provider.{OSMReverseGeocodingProvider, OpenMeteoWeatherProvider, SettingsProvider}
import pl.edu.agh.congard.weatherapp.backend.unit.{MeasureUnit, TemperatureUnit}
import pl.edu.agh.congard.weatherapp.gui.FxApp

import java.io.File
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main {
    def main(args: Array[String]): Unit =
        Application.launch(classOf[FxApp], args:_*)
}
