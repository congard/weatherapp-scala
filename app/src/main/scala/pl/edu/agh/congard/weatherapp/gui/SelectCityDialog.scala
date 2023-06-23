package pl.edu.agh.congard.weatherapp.gui

import javafx.application.Platform
import javafx.collections.{FXCollections, ObservableList}
import javafx.scene.Node
import javafx.scene.control.{Label, ListCell, ListView, TextField}
import javafx.scene.layout.VBox
import javafx.util.Callback
import pl.edu.agh.congard.weatherapp.backend.GeoPlaceDetails
import pl.edu.agh.congard.weatherapp.backend.ext.{also, ifNotNull}
import pl.edu.agh.congard.weatherapp.backend.provider.OSMReverseGeocodingProvider

import java.util
import java.util.LinkedList
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class SelectCityDialog(onCitySelected: GeoPlaceDetails => Unit) extends Dialog {
    private val items = FXCollections.observableList(util.LinkedList[GeoPlaceDetails])

    setTitle("Select city")

    setHeader(VBox().also { box =>
        box.getChildren().addAll(
            Label("Select city"),
            TextField().also { it =>
                it.setPromptText("City name")

                it.setOnKeyTyped { e => 
                    OSMReverseGeocodingProvider().search(it.getText()).onComplete {
                        case Success(value) => items.synchronized {
                            items.clear()
                            val unique = Set(value: _*)
                            unique.foreach { it => items.add(it) }
                        }
                        case Failure(exception) =>
                            Platform.runLater(() => ErrorDialog("OSMReverseGeocodingProvider", exception))
                            exception.printStackTrace()
                    }
                }
            }
        )
    })

    setContent(ListView[GeoPlaceDetails]().also { it =>
        it.setItems(items)

        it.setOnMouseClicked(event => if (event.getClickCount() == 2) {
            it.getSelectionModel().getSelectedItem().ifNotNull { it =>
                onCitySelected(it)
                close()
            }
        })

        it.setCellFactory(new Callback[ListView[GeoPlaceDetails], ListCell[GeoPlaceDetails]] {
            def call(param: ListView[GeoPlaceDetails]): ListCell[GeoPlaceDetails] = new ListCell[GeoPlaceDetails] {
                override protected def updateItem(item: GeoPlaceDetails, empty: Boolean): Unit = {
                    super.updateItem(item, empty)

                    Platform.runLater(() => {
                        if (item != null) {
                            setText(item.displayName)
                        } else {
                            setText("")
                            setGraphic(null)
                        }
                    })
                }
            }
        })
    })
}
