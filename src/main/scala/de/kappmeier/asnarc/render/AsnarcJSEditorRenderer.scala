package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.board.AsnarcBoard
import de.kappmeier.asnarc.elements.Element
import de.kappmeier.asnarc.render.AsnarcJSRenderer.stripSimpleName
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom
import org.scalajs.dom.html

/**
  * Draws the Asnarc game into a canvas.
  *
  * @param boardCanvas the HTML canvas element for the game board
  * @param detailsCanvas the HTML canvas element for showing element details
  * @param loc localization for text rendering
  * @param config renderer configuration with block size and derived values
  */
class AsnarcJSEditorRenderer(boardCanvas: html.Canvas, detailsCanvas: html.Canvas,
                             loc: AsnarcLocalization, config: AsnarcJSRenderer)
    extends AbstractAsnarcJSRenderer(boardCanvas, loc, config) {

  val rendererDetails: dom.CanvasRenderingContext2D = detailsCanvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val scale: Double = detailsCanvas.width.toDouble / config.Size

  def renderBoard(board: AsnarcBoard, info: String): Unit = {
    clear()
    this.drawBoard(board)
    renderInfo(board, info, "")
  }

  def highlight(x: Int, y: Int) = {
    highlightElement(x, y)
  }

  def highlightElement(element: Element): Unit = {
    rendererDetails.clearRect(0, 0, detailsCanvas.width, detailsCanvas.height)
    if (element.p.x >= 0 && element.p.y >= 0) {
      val color = AsnarcJSRenderer.DrawColors.getOrElse(stripSimpleName(element.getClass.getSimpleName), "black")
      rendererDetails.fillStyle = color
      AbstractAsnarcJSRenderer.fillElementAt(rendererDetails, 0, 0, element, config, scale)
      highlightElement(element.p.x, element.p.y)
    }
  }

  def highlightElement(xPosition: Int, yPosition: Int): Unit = {
    renderer.strokeStyle = "red"
    renderer.lineWidth = 2
    val x: Int = xPosition * config.Size
    val y: Int = yPosition * config.Size
    val w = config.DrawSize
    val h = config.DrawSize
    renderer.strokeRect(x, y, w, h)
  }
}
