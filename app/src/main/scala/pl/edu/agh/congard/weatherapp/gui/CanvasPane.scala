package pl.edu.agh.congard.weatherapp.gui

import javafx.scene.canvas.{Canvas, GraphicsContext}
import javafx.scene.layout.Pane

abstract class CanvasPane extends Pane {
    private val canvas = Canvas()
    getChildren().add(canvas)

    final def draw(): Unit = {
        val g = canvas.getGraphicsContext2D()
        g.clearRect(0, 0, canvas.getWidth(), canvas.getHeight())
        onDraw(g)
    }

    protected def onDraw(g: GraphicsContext): Unit

    protected def availableWidth(): Double = canvas.getWidth()
    protected def availableHeight(): Double = canvas.getHeight()

    override def layoutChildren(): Unit = {
        val top = snappedTopInset()
        val right = snappedRightInset()
        val bottom = snappedBottomInset()
        val left = snappedLeftInset()

        val w = getWidth() - left - right
        val h = getHeight() - top - bottom

        canvas.setLayoutX(left)
        canvas.setLayoutY(top)

        canvas.setWidth(w)
        canvas.setHeight(h)

        draw()
    }
}
