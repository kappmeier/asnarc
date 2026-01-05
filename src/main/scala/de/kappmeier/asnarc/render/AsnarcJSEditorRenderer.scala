package de.kappmeier.asnarc.render

import org.scalajs.dom.html

import de.kappmeier.asnarc.board.AsnarcBoard
import de.kappmeier.asnarc.render.localization.AsnarcLocalization

/**
  * Draws the Asnarc game board into a canvas.
  */
class AsnarcJSEditorRenderer(boardCanvas: html.Canvas, loc: AsnarcLocalization
                            ) extends AbstractAsnarcJSRenderer(boardCanvas, loc) {

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
    val x: Int = xPosition * AsnarcJSRenderer.Size;
    val y: Int = yPosition * AsnarcJSRenderer.Size;
    val w = AsnarcJSRenderer.DrawSize
    val h = AsnarcJSRenderer.DrawSize
    renderer.strokeRect(x, y, w, h)
  }
}
