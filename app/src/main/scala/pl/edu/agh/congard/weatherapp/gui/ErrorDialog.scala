package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.control.Alert
import javafx.scene.control.Alert.AlertType

class ErrorDialog(header: String, exception: Throwable) extends Alert(AlertType.ERROR) {
    getDialogPane().getStylesheets().add("styles.css")
    setTitle("Error")
    setHeaderText(header)
    setContentText(if exception != null then f"Message: ${exception.getMessage()}" else "")
    showAndWait()
}
