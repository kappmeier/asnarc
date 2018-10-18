package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.board.AsnarcBoard
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom.html

/**
  * Draws the Asnarc game into a canvas.
  */
class AsnarcJSEditorRenderer(canvas: html.Canvas, loc: AsnarcLocalization) extends AbstractAsnarcJSRenderer(canvas, loc) {

  def render(board: AsnarcBoard, info: String): Unit = {
    clear()
    this.drawBoard(board)
    renderInfo(board, info, "")
  }
}
