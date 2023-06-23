package pl.edu.agh.congard.weatherapp.gui

import javafx.geometry.VPos
import javafx.scene.SnapshotParameters
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.Tooltip
import javafx.scene.effect.ColorInput
import javafx.scene.image.{Image, ImageView}
import javafx.scene.paint.{Color, CycleMethod, LinearGradient, Stop}
import javafx.scene.text.{Font, TextAlignment}
import javafx.scene.transform.Affine
import javafx.util.Duration
import pl.edu.agh.congard.weatherapp.backend.WeatherDetails
import pl.edu.agh.congard.weatherapp.backend.ext.let
import pl.edu.agh.congard.weatherapp.backend.unit.TemperatureUnit
import pl.edu.agh.congard.weatherapp.math.CubicSpline

import java.io.ByteArrayInputStream
import java.nio.ByteBuffer
import scala.collection.immutable.NumericRange
import scala.math.BigDecimal
import scala.math.Ordering.*

class WeatherChart extends CanvasPane {
    private var forecast: Seq[WeatherDetails] = List()

    setMinHeight(WeatherChart.height)
    setMaxHeight(WeatherChart.height)

    Tooltip().let { tooltip =>
        Tooltip.install(this, tooltip)

        tooltip.setShowDuration(Duration(60_000))

        setOnMouseMoved { e =>
            tooltip.setX(e.getScreenX())
            tooltip.setY(e.getScreenY())
            
            val i = (e.getX() / WeatherChart.chunkWidth).toInt
            if i < forecast.length then updateTooltip(tooltip, forecast(i))
        }
    }

    override protected def onDraw(g: GraphicsContext): Unit = {
        // g.setFill(new LinearGradient(
        //     0, 0, 1, 1, true,                      //sizing
        //     CycleMethod.NO_CYCLE,                  //cycling
        //     new Stop(0, Color.web("#81c483")),     //colors
        //     new Stop(1, Color.web("#fcc200"))))
        // g.fillRect(0, 0, availableWidth(), availableHeight())

        drawPrecipitation(g)
        drawMesh(g)
        drawDescription(g)
        drawSplines(g)
    }

    /**
      * Note: must be called from the UI thread
      * @param _forecast
      */
    def setForecast(_forecast: Seq[WeatherDetails]): Unit = {
        forecast = _forecast
        setMinWidth(WeatherChart.chunkWidth * forecast.length)
        draw()
    }

    private def mapChartY(y: Double): Double = 
        y + WeatherChart.descriptionHeight

    private def drawMesh(g: GraphicsContext): Unit = {
        g.setStroke(WeatherChart.meshColor)
        g.setLineWidth(0.5)

        // vertically
        for (i <- 0 to forecast.length) {
            val x = i * WeatherChart.chunkWidth
            g.strokeLine(x, mapChartY(0), x, mapChartY(WeatherChart.chartHeight))
        }

        // horizontally
        for (i <- 0 to WeatherChart.horizontalLineCount) {
            val y = mapChartY(WeatherChart.chartHeight * i / WeatherChart.horizontalLineCount)
            g.strokeLine(0, y, availableWidth(), y)
        }
    }

    private def drawDescription(g: GraphicsContext): Unit = {
        g.setTextAlign(TextAlignment.CENTER)
        g.setTextBaseline(VPos.CENTER)
        g.setFont(Font(9))
        g.setFill(WeatherChart.textColor)

        for (i <- forecast.indices) {
            forecast(i).precipitation.let { it =>
                if (it.value != 0.0) {
                    g.fillText(
                        it.toString,
                        WeatherChart.chunkWidth * (0.5 + i),
                        WeatherChart.descriptionHeight / 2)
                }
            }

            g.fillText(
                forecast(i).date.let { it => f"${it.getHour()} / ${it.getDayOfMonth()}" },
                WeatherChart.chunkWidth * (0.5 + i),
                WeatherChart.height - WeatherChart.descriptionHeight / 2)
        }
    }

    private def drawSplines(g: GraphicsContext): Unit = {
        if forecast.isEmpty then return

        val minTemp = forecast.minBy(_.temp.value).temp
        val maxTemp = forecast.maxBy(_.temp.value).temp

        val splineXNodes = forecast.indices.map(_.toDouble)
        val splineXs = (BigDecimal(0.0) to BigDecimal(forecast.length - 1) by BigDecimal(0.1)).toList.map(_.doubleValue)
        
        val tempValues = forecast.map(_.temp.value)
        val tempSpline = CubicSpline(splineXNodes, tempValues)

        def mapY(y: Double, minY: Double, maxY: Double): Double =
            mapChartY(WeatherChart.splineVPadding + (WeatherChart.chartHeight - 2 * WeatherChart.splineVPadding) * (1 - (y - minY) / (maxY - minY)))

        def drawSpline(spline: Double => Double, minVal: Double, maxVal: Double): Unit = {
            g.beginPath()
            g.moveTo(splineXs.head, mapY(spline(splineXs.head), minVal, maxVal))

            for (_x <- splineXs.tail) {
                val x = _x * WeatherChart.chunkWidth
                val y = mapY(spline(_x), minVal, maxVal)
                g.lineTo(x, y)
                g.moveTo(x, y)
            }

            g.stroke()
        }
        
        g.setStroke(WeatherChart.tempColor)
        g.setLineWidth(3)
        drawSpline(tempSpline, minTemp.value, maxTemp.value)
    }

    private def drawPrecipitation(g: GraphicsContext): Unit = {
        if forecast.isEmpty then return
        
        val precMinMm = forecast.minBy(_.precipitation.toMillimeters.value).precipitation.toMillimeters
        val precMaxMm = forecast.maxBy(_.precipitation.toMillimeters.value).precipitation.toMillimeters

        g.setFill(WeatherChart.precColor)

        for (i <- forecast.indices) {
            val prec = forecast(i).precipitation
            val precMm = prec.toMillimeters

            val height = WeatherChart.chartHeight * (precMm.mm - precMinMm.mm) / (precMaxMm.mm - precMinMm.mm)

            g.fillRect(
                i * WeatherChart.chunkWidth,
                mapChartY(WeatherChart.chartHeight - height),
                WeatherChart.chunkWidth,
                height
            )
        }
    }

    private def updateTooltip(tooltip: Tooltip, weather: WeatherDetails): Unit = {
        tooltip.setText(
            f"Forecast for: ${weather.date}\n" +
            f"Temperature: ${weather.temp}\n" +
            f"Feels like: ${weather.apparentTemp}\n" +
            f"Humidity: ${weather.humidity}\n" +
            f"Precipitation: ${weather.precipitation}\n" +
            f"Precipitation probability: ${weather.precipitationProbability}\n" +
            f"Humidity: ${weather.humidity}\n" +
            (if weather.rain.value != 0 then f"Rain: ${weather.rain}\n" else "") +
            (if weather.showers.value != 0 then f"Showers: ${weather.showers}\n" else "") +
            (if weather.snowfall.value != 0 then f"Snowfall: ${weather.snowfall}\n" else "")
        )
    }
}

object WeatherChart {
    private val height: Double = 160
    private val descriptionHeight: Double = 16 // top and bottom
    private val chartHeight: Double = height - descriptionHeight * 2
    private val chunkWidth: Double = 36

    private val splineVPadding: Double = 16

    private val horizontalLineCount: Int = 5

    private val tempColor = Color.web("#9a1214")
    private val precColor = Color.web("#778C99")
    private val meshColor = Color.web("#5a5a5a")
    private val textColor = Color.web("#808080")
}
