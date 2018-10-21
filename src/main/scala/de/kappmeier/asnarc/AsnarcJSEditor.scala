package de.kappmeier.asnarc

import de.kappmeier.asnarc.board.{AsnarcBoard, Point}
import de.kappmeier.asnarc.render.{AsnarcJSEditorRenderer, AsnarcJSRenderer}
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe
import org.scalajs.dom
import org.scalajs.dom.html

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

/**
  * The ScalaJS export for Asnarc editor. Renders the board, waits for user input and updates the board.
  *
  * The visualization consists of a rectangular field.
  */
@JSExportTopLevel("AsnarcJSEditor")
object AsnarcJSEditor {


  @JSExport
  def main(canvas: html.Canvas, level: String): Unit = {
    val board: AsnarcBoard = new AsnarcBoard(level)

    val localization = new AsnarcLocalizationDe
    val detailsCanvas: html.Canvas = dom.document.getElementById("canvas-details").asInstanceOf[html.Canvas]
    val renderer: AsnarcJSEditorRenderer = new AsnarcJSEditorRenderer(canvas, detailsCanvas, localization)

    renderer.renderBoard(board, "")

    canvas.onclick = (e: dom.MouseEvent) => {
      val x: Int = e.clientX.asInstanceOf[Int] / AsnarcJSRenderer.Size
      val y = e.clientY.asInstanceOf[Int] / AsnarcJSRenderer.Size
      if (x < board.cols && y < board.rows) {
        renderer.renderBoard(board, "Click: " + board.elementAt(Point(x,y)) + " at " + x + "," + y)
        renderer.highlight(x, y)
        renderer.highlightElement(board.elementAt(Point(x, y)))
      }

    }

    canvas.onkeydown = (e: dom.KeyboardEvent) => {
    }
  }

}
