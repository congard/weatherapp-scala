package pl.edu.agh.congard.weatherapp.gui

import javafx.application.Application
import javafx.stage.Stage

class FxApp extends Application {
    assert(!FxApp.hasInstance)
    FxApp.instance = this

    override def start(primaryStage: Stage): Unit =
        AppStage().show()
}

object FxApp {
    private var _instance: Option[FxApp] = None

    def hasInstance: Boolean = _instance.isDefined

    // public accessor
    def instance: FxApp = _instance.get

    // private mutator
    private def instance_=(instance: FxApp): Unit = { _instance = Some(instance) }
}
