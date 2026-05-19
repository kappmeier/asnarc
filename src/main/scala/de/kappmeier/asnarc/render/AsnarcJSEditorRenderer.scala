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

  /**
    * Highlights an element upon selection. Adds a visible border.
    *
    * @param xPosition the horizontal index of the element on the board
    * @param yPosition the vertical index of the element on the board
    */
  private def highlightElement(xPosition: Int, yPosition: Int): Unit = {
    renderer.strokeStyle = "red"
    val lineWidth: Int = 2
    renderer.lineWidth = lineWidth
    val inset: Int = lineWidth / 2
    val x: Int = xPosition * config.Size + inset
    val y: Int = yPosition * config.Size + inset
    val w = config.Size - lineWidth
    val h = config.Size - lineWidth
    renderer.strokeRect(x, y, w, h)
  }
}
