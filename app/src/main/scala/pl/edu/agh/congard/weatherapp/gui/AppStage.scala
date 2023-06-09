package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.{Node, Scene}
import javafx.scene.control.{Button, Label, ScrollPane, Tooltip}
import javafx.scene.layout.{BorderPane, VBox}
import javafx.scene.text.{Text, TextFlow}
import javafx.stage.Stage
import pl.edu.agh.congard.weatherapp.backend.ext.ScopeFunExt
import pl.edu.agh.congard.weatherapp.backend.provider.SettingsProvider
import javafx.scene.image.Image

/**
  * The main stage of this app
  */
class AppStage extends Stage {
    private val contentBox = VBox().also { it =>
        it.setId("contentBox")

        SettingsProvider.places.foreach { place =>
            it.getChildren().add(WeatherPane(place))
        }
    }

    init()

    private def init(): Unit = {
        setTitle("WeatherApp")
        setWidth(512)
        setHeight(760)

        getIcons().add(Image(getClass().getResourceAsStream("/icon.png")))

        val mainBox = BorderPane()
        mainBox.setId("mainScreen")

        mainBox.setCenter(createContent())
        mainBox.setBottom(createFooter())

        val scene = Scene(mainBox)
        scene.getStylesheets.add("styles.css")
        setScene(scene)
    }

    private def createContent(): Node = ScrollPane().also { scrollPane =>
        scrollPane.setFitToWidth(true)
        scrollPane.setFitToHeight(true)
        scrollPane.setId("mainScrollPane")
        scrollPane.setContent(contentBox)
    }

    private def createFooter(): Node = BorderPane().also { footer =>
        footer.setId("app-footer")

        footer.setCenter(Button().also { it =>
            it.setGraphic(Resources.icAdd)
            it.setTooltip(Tooltip("Add"))
            it.setOnAction(_ => {
                SelectCityDialog(onCitySelected = details => {
                    SettingsProvider.addPlace(details)
                    contentBox.getChildren().add(WeatherPane(details))
                }).show()
            })
        })

        footer.setLeft(Button().also { it =>
            it.setGraphic(Resources.icUpdate)
            it.setTooltip(Tooltip("Refresh"))
            it.setOnAction { _ => contentBox.getChildren().forEach { child => child.asInstanceOf[WeatherPane].update() } }
        })

        footer.setRight(Button().also { it =>
            it.setGraphic(Resources.icInfo)
            it.setTooltip(Tooltip("Info"))
            it.setOnAction { _ => showAbout() }
        })
    }

    private def showAbout(): Unit = Stage().let { stage =>
        stage.setTitle("About WeatherApp")
        stage.setWidth(450)
        stage.setHeight(350)

        val content = VBox().also { it =>
            it.getStyleClass().add("about-container")

            it.getChildren().add(Label("WeatherApp"))

            it.getChildren().add(TextFlow(
                Text("A simple weather app written in Scala\n\n"),
                Text("This project is developed by congard within the subject " +
                    "of Scala Programming Language at the AGH University of Science and Technology\n\n" +
                    "Distributed under the BSD 4-clause license\n\n" +
                    "Homepage: "), Link("https://github.com/congard/weatherapp-scala"),
                Text("\nMy GitHub:"), Link("https://github.com/congard")
            ).also(_.getStyleClass().add("text-flow")))
        }

        val scene = Scene(content)
        scene.getStylesheets().add("styles.css")
        stage.setScene(scene)

        stage.show()
    }
}
