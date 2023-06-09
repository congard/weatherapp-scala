package pl.edu.agh.congard.weatherapp.gui

import javafx.application.Platform
import javafx.geometry.Insets
import javafx.scene.control.{Button, Label, ScrollPane, Tooltip}
import javafx.scene.layout.{BorderPane, Pane, Priority, VBox}
import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails
import pl.edu.agh.congard.weatherapp.backend.ext.ScopeFunExt
import pl.edu.agh.congard.weatherapp.backend.provider.{OpenMeteoWeatherProvider, SettingsProvider, WeatherProvider}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class WeatherPane(
    details: GeoPlaceDetails,
    provider: WeatherProvider = OpenMeteoWeatherProvider()
) extends VBox {
    private val chart = WeatherChart()

    provider.place = details

    getStyleClass().add("weather-pane")

    getChildren().addAll(
        BorderPane().also { borderPane =>
            borderPane.getStyleClass().add("header")

            borderPane.setLeft(Label(details.displayName))

            borderPane.setRight(Button("–").also { it =>
                it.setTooltip(Tooltip("Remove"))
                it.setOnAction { _ =>
                    getParent().asInstanceOf[Pane].getChildren().remove(this)
                    SettingsProvider.removePlace(details)
                }
            })
        },
        ScrollPane().also { it =>
            it.setPadding(Insets(0))
            it.setFitToWidth(true)
            it.setMaxHeight(chart.getMaxHeight())
            it.setMinHeight(chart.getMaxHeight())
            it.setContent(chart)
            it.hbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER)
            it.vbarPolicyProperty().setValue(ScrollPane.ScrollBarPolicy.NEVER)
        }
    )

    update()

    def update(): Unit = provider.update.onComplete {
        case Success(_) => Platform.runLater(() => chart.setForecast(provider.getWeatherFromNow))
        case Failure(exception) =>
            Platform.runLater(() => ErrorDialog("Weather data cannot be obtained", exception))
            exception.printStackTrace()
    }
}
