package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.control.Hyperlink

class Link(text: String) extends Hyperlink(text) {
    setOnAction { _ => FxApp.instance.getHostServices().showDocument(text) }
}
