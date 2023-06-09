package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.layout.{BorderPane, VBox}
import javafx.scene.{Node, Scene}
import javafx.stage.Stage
import pl.edu.agh.congard.weatherapp.backend.ext.ScopeFunExt

abstract class Dialog extends Stage {
    private val content = BorderPane().also { it => it.setId("dialog") }

    setScene(Scene(content).also { scene =>
        scene.getStylesheets.add("styles.css")
    })

    setTitle("Dialog")
    setWidth(400)
    setHeight(600)
    
    protected def setHeader(node: Node): Unit = {
        node.getStyleClass().add("dialog-header")
        content.setTop(node)
    }
    
    protected def setContent(node: Node): Unit = {
        node.getStyleClass().add("dialog-content")
        content.setCenter(node)
    }

    protected def setFooter(node: Node): Unit = {
        node.getStyleClass().add("dialog-footer")
        content.setBottom(node)
    }
}
