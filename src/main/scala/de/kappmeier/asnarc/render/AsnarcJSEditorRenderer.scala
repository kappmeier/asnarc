package de.kappmeier.asnarc.render

import org.scalajs.dom.html

import de.kappmeier.asnarc.board.AsnarcBoard
import de.kappmeier.asnarc.render.localization.AsnarcLocalization

/**

  * Draws the Asnarc game into a canvas.
  *
  * @param boardCanvas the HTML canvas element for the game board
  * @param loc localization for text rendering
  * @param config renderer configuration with block size and derived values
  */
class AsnarcJSEditorRenderer(boardCanvas: html.Canvas, loc: AsnarcLocalization, config: AsnarcJSRenderer)
    extends AbstractAsnarcJSRenderer(boardCanvas, loc, config) {

  def renderBoard(board: AsnarcBoard, info: String): Unit = {
    clear()
    this.drawBoard(board)
    renderInfo(board, info, "")
  }

  def highlight(x: Int, y: Int): Unit = {
    highlightElement(x, y)
  }

  private def highlightElement(xPosition: Int, yPosition: Int): Unit = {
    renderer.strokeStyle = "red"
    renderer.lineWidth = 2
    val x: Int = xPosition * config.Size
    val y: Int = yPosition * config.Size
    val w = config.DrawSize
    val h = config.DrawSize
    renderer.strokeRect(x, y, w, h)
  }
}
