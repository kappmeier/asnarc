package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.board.{AsnarcBoard, Direction}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom
import org.scalajs.dom.html

/**
  * Provides helper methods for the canvas to draw on.
  */
class AbstractAsnarcJSRenderer(canvas: html.Canvas, loc: AsnarcLocalization) {

  val renderer: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  canvas.width = canvas.parentElement.clientWidth
  canvas.height = canvas.parentElement.clientHeight

  /**
    * Draws a clean background. Should be called before an update is drawn.
    */
  def clear(): Unit = {
    renderer.clearRect(0, 0, canvas.width, canvas.height)
  }

  /**
    * Sets the current draw font style.
    *
    * @param rendererTextStyle the font and size
    */
  def switchStyle(rendererTextStyle: RendererTextStyle): Unit = {
    val formatString: String = "%dpx %s".format(rendererTextStyle.size, rendererTextStyle.font)
    renderer.font = formatString
    renderer.fillStyle = rendererTextStyle.color
  }

  /**
    * Draws an [AsnarcBoard] in the upper left corner of the canvas.
    *
    * @param board the game board
    */
  def drawBoard(board: AsnarcBoard): Unit = {
    for {(_, e) <- board.map} {
      val color = AsnarcJSRenderer.DrawColors.getOrElse(e.getClass.getSimpleName, "yellow")
      renderer.fillStyle = color
      fillElement(e)
    }

  }

  /**
    * Fills the square block of an [[Element]] of the Asnarc game. For a block standing alone only the interior is filled,
    * but when a block is connected with the neighbor block in a specific direction, the border in this direction is also filled.
    *
    * Whether the block is connected with a neighbor is not defined by two block being actually neighbors. Instead, it
    * is enough that the `connects` property of the [[Element]] is set.
    *
    * @param location the location of the [[Element]] that is filled
    */
  def fillElement(location: Element): Unit = {
    val x = if (location.connects contains Direction.Left) location.p.x * AsnarcJSRenderer.Size else location.p.x * AsnarcJSRenderer.Size + AsnarcJSRenderer.Border
    val y = if (location.connects contains Direction.Up) location.p.y * AsnarcJSRenderer.Size else location.p.y * AsnarcJSRenderer.Size + AsnarcJSRenderer.Border
    val w = AsnarcJSRenderer.DrawSize + (if (location.connects contains Direction.Left) 1 else 0) + (if (location.connects contains Direction.Right) 1 else 0)
    val h = AsnarcJSRenderer.DrawSize + (if (location.connects contains Direction.Up) 1 else 0) + (if (location.connects contains Direction.Down) 1 else 0)
    renderer.fillRect(x, y, w, h)
  }

  /**
    * Prints informational text right below the game board.
    *
    * @param board the board, used to calculate the position
    * @param left  the text under the lower left corner
    * @param right the text under the lower right corner
    */
  def renderInfo(board: AsnarcBoard, left: String, right: String): Unit = {
    switchStyle(AsnarcJSRenderer.InformationStyle)
    renderer.textBaseline = "top"
    renderInfoLeft(board, left)
    renderInfoRight(board, right)
  }

  private def renderInfoLeft(board: AsnarcBoard, leftString: String): Unit = {
    renderer.textAlign = "left"
    renderer.fillText(leftString, AsnarcJSRenderer.Border, board.rows * AsnarcJSRenderer.Size + AsnarcJSRenderer.Border)
  }

  private def renderInfoRight(board: AsnarcBoard, rightString: String): Unit = {
    renderer.textAlign = "right"
    renderer.fillText(rightString, AsnarcJSRenderer.Size * board.cols - AsnarcJSRenderer.Border,
      board.rows * AsnarcJSRenderer.Size + AsnarcJSRenderer.Border)
  }

}
