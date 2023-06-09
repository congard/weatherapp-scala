package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.paint.Color
import javafx.scene.shape.SVGPath
import pl.edu.agh.congard.weatherapp.backend.ext.ScopeFunExt

object Resources {
    val icAdd = SVGPath().also { it =>
        it.setContent("M 19 9 L 11 9 L 11 1 L 9 1 L 9 9 L 1 9 L 1 11 L 9 11 L 9 19 L 11 19 L 11 11 L 19 11 L 19 9")
        it.setFill(Color.web("#ffffff"))
    }

    val icUpdate = SVGPath().also { it =>
        it.setContent("M15,6V1.76l-1.7,1.7A7,7,0,1,0,14.92,9H13.51a5.63,5.63,0,1,1-1.2-4.55L10.76,6Z")
        it.setFill(Color.web("#ffffff"))
    }

    val icInfo = SVGPath().also { it =>
        it.setContent("M7.92,5.32v0A.92.92,0,1,0,7,4.37v0A.92.92,0,0,0,7.92,5.32ZM8.85,6H6.55a4.58,4.58,0,0,1-.1.63,3,3,0,0,1-.2.67h1.3v3.53H6.25v1.3H9.57v-1.3H8.85ZM13,3.05A7,7,0,1,0,13,13,7,7,0,0,0,13,3.05ZM12,12A5.6,5.6,0,0,1,4,12,5.61,5.61,0,0,1,4,4,5.6,5.6,0,0,1,12,4,5.61,5.61,0,0,1,12,12Z")
        it.setFill(Color.web("#ffffff"))
    }
}
