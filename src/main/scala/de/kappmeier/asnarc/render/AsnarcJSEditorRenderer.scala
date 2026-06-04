package de.kappmeier.asnarc.render

import org.scalajs.dom.html

import de.kappmeier.asnarc.board.AsnarcBoard
import de.kappmeier.asnarc.elements.Teleport
import de.kappmeier.asnarc.render.localization.AsnarcLocalization

/**
  * Draws the Asnarc game into a canvas.
  *
  * @param boardCanvas the HTML canvas element for the game board
  * @param loc         localization for text rendering
  * @param config      renderer configuration with block size and derived values
  */
class AsnarcJSEditorRenderer(boardCanvas: html.Canvas, loc: AsnarcLocalization, config: AsnarcJSRenderer)
  extends AbstractAsnarcJSRenderer(boardCanvas, loc, config) {

  def renderBoard(board: AsnarcBoard, info: String): Unit = {
    clear()
    this.drawBoard(board)
    renderEditorSpecificOverlay(board)
    renderInfo(board, info, "")
  }

  /**
    * Editor specific overlay.
    *
    * Marks unpaired teleports.
    *
    * @param board the game board
    */
  private def renderEditorSpecificOverlay(board: AsnarcBoard): Unit = {
    board.staticMap.collect { case (p, t: Teleport) if t.target.isEmpty => p }
      .foreach(p => hatchCell(p.x, p.y))
  }

  /**
    * Overlays a diagonal hatch pattern on a single cell, clipped to the cell bounds. Used in the editor to mark
    * unpaired teleports so they are visually distinct from paired (solid) ones.
    *
    * @param x the horizontal index of the cell on the board
    * @param y the vertical index of the cell on the board
    */
  private def hatchCell(x: Int, y: Int): Unit = {
    val px = x * config.Size
    val py = y * config.Size
    renderer.save()
    renderer.beginPath()
    renderer.rect(px, py, config.Size, config.Size)
    renderer.clip()
    renderer.strokeStyle = "white"
    renderer.lineWidth = 1
    val spacing = 4
    (-config.Size until config.Size by spacing).foreach { step =>
      renderer.beginPath()
      renderer.moveTo(px + step, py)
      renderer.lineTo(px + step + config.Size, py + config.Size)
      renderer.stroke()
    }
    renderer.restore()
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
