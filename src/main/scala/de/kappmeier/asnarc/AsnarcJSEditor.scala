package de.kappmeier.asnarc

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

import org.scalajs.dom
import org.scalajs.dom.html

import de.kappmeier.asnarc.board.{AsnarcBoard, Point}
import de.kappmeier.asnarc.levels.PredefinedLevels
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe
import de.kappmeier.asnarc.render.{AsnarcJSEditorRenderer, AsnarcJSRenderer}

/**
  * The ScalaJS export for Asnarc editor. Renders the board, waits for user input and updates the board.
  *
  * The visualization consists of a rectangular field.
  *
  * The level parameter can be either:
  * - A named level (e.g., "Empty", see [[PredefinedLevels]] for available levels)
  * - A raw Base64 encoded level string
  */
@JSExportTopLevel("AsnarcJSEditor")
object AsnarcJSEditor {

  @JSExport
  def main(canvas: html.Canvas, level: String): Unit = {
    // Resolve level input - either a named level or raw level data
    val resolvedLevel: String = PredefinedLevels.resolve(level) match {
      case Some(decodedLevel) => {
        dom.console.log(s"Using named level: ${level}")
        decodedLevel
      }
      case None => {
        dom.console.log(s"Using custom level data")
        level
      }
    }

    val board: AsnarcBoard = new AsnarcBoard(resolvedLevel)

    // Create renderer configuration with default block size
    val rendererConfig = new AsnarcJSRenderer()

    val localization = new AsnarcLocalizationDe
    val detailsCanvas: html.Canvas = dom.document.getElementById("canvas-details").asInstanceOf[html.Canvas]
    val renderer: AsnarcJSEditorRenderer = new AsnarcJSEditorRenderer(canvas, detailsCanvas, localization, rendererConfig)

    renderer.renderBoard(board, "")

    canvas.onclick = (e: dom.MouseEvent) => {
      val x: Int = e.clientX.asInstanceOf[Int] / rendererConfig.Size
      val y = e.clientY.asInstanceOf[Int] / rendererConfig.Size
      if (x < board.cols && y < board.rows) {
        renderer.renderBoard(board, "Click: " + board.elementAt(Point(x, y)) + " at " + x + "," + y)
        renderer.highlight(x, y)
        renderer.highlightElement(board.elementAt(Point(x, y)))
      }

    }

    canvas.onkeydown = (e: dom.KeyboardEvent) => {
    }
  }

}
