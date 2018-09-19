package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcState._
import de.kappmeier.asnarc.board.Direction
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcGameImpl}
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom
import org.scalajs.dom.html

import scala.collection.immutable.HashMap

/**
  * Structure containing the font style for text rendered.
  *
  * @param font  the font style as html, i.e. "sans-serif"
  * @param size  the size in pixels
  * @param color the font color, i.e. #ff0000 or "darkgreen"
  */
case class RendererTextStyle(font: String, size: Int, color: String)

/**
  * Draws the Asnarc game into a canvas.
  */
class AsnarcJSRenderer(canvas: html.Canvas, loc: AsnarcLocalization) {

  val drawColors: HashMap[String, String] = collection.immutable.HashMap[String, String](
    Food.getClass.getSimpleName -> "darkred",
    Wall.getClass.getSimpleName -> "black",
    SnakeBody.getClass.getSimpleName -> "darkgreen",
    SpecialFood.getClass.getSimpleName -> "orange",
    SnakeHead.getClass.getSimpleName -> "green"
  )

  val renderer: dom.CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

  val gameOverFont: RendererTextStyle = RendererTextStyle("sans-serif", 20, "darkred")
  val informationStyle: RendererTextStyle = RendererTextStyle("sans-serif", 12, "black")
  val infoFont: RendererTextStyle = RendererTextStyle("sans-serif", 20, "black")

  canvas.width = canvas.parentElement.clientWidth
  canvas.height = canvas.parentElement.clientHeight

  def switchStyle(rendererTextStyle: RendererTextStyle): Unit = {
    val formatString: String = "%dpx %s".format(rendererTextStyle.size, rendererTextStyle.font)
    renderer.font = formatString
    renderer.fillStyle = rendererTextStyle.color
  }

  val radius: Int = 5
  val border: Int = 1
  val drawRadius: Int = radius - border
  val drawSize: Int = 2 * drawRadius
  val size: Int = 2 * radius

  def render(snakeGame: AsnarcGameImpl, asnarcState: AsnarcState) {
    renderer.clearRect(0, 0, canvas.width, canvas.height)

    asnarcState match {
      case Started => // start the game
        renderStart(snakeGame)
      case Running =>
        renderMove(snakeGame)
        renderInfo(snakeGame)
      case GameOver =>
        renderMove(snakeGame)
        renderGameOver(snakeGame)
      // restart the game
      case Pause => // continue the game
        renderMove(snakeGame)
        renderPause(snakeGame)
        renderInfo(snakeGame)
    }
  }

  def renderStart(snakeGame: AsnarcGameImpl): Unit = {
    val color = drawColors.getOrElse(Wall.getClass.getSimpleName, "black")
    renderer.fillStyle = color
    snakeGame.state.board.map.withFilter { tuple => tuple._2.isInstanceOf[Wall] }.foreach { tuple => fillElement(tuple._2) }

    switchStyle(infoFont)
    drawCenterText(snakeGame, loc.stateMessageStart)
  }

  def renderMove(game: AsnarcGame): Unit = {
    // Draw background
    for {(_, e) <- game.state.board.map} {
      val color = drawColors.getOrElse(e.getClass.getSimpleName, "yellow")
      renderer.fillStyle = color
      fillElement(e)
    }
    renderer.fillStyle = drawColors(SnakeHead.getClass.getSimpleName)
    fillElement(game.state.player.head)
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
    val x = if (location.connects contains Direction.Left) location.p.x * size else location.p.x * size + border
    val y = if (location.connects contains Direction.Up) location.p.y * size else location.p.y * size + border
    val w = drawSize + (if (location.connects contains Direction.Left) 1 else 0) + (if (location.connects contains Direction.Right) 1 else 0)
    val h = drawSize + (if (location.connects contains Direction.Up) 1 else 0) + (if (location.connects contains Direction.Down) 1 else 0)
    renderer.fillRect(x, y, w, h)
  }

  /**
    * Displays the status message below the game canvas. The status contains some statistics (current length, points,
    * average turns, ...) and the current position of the head.
    *
    * @param game the game canvas
    */
  def renderInfo(game: AsnarcGame): Unit = {
    val len: Int = game.state.player.body.size + 1
    switchStyle(informationStyle)
    renderer.textAlign = "left"
    renderer.textBaseline = "top"
    val averageTime = game.frame / len
    val averageTurns = game.turns / len
    val infoString = loc.statusText.format(len, 0, averageTime, averageTurns)
    renderer.fillText(infoString, border, game.state.board.rows * size + border)
    renderer.textAlign = "right"
    val locString = loc.statusPosition.format(game.state.player.head.p.x, game.state.player.head.p.y)
    renderer.fillText(locString, size * game.state.board.cols - border, game.state.board.rows * size + border)
  }

  /**
    * Displays the pause message on top of the game.
    *
    * @param snakeGame the board canvas
    */
  def renderPause(snakeGame: AsnarcGameImpl): Unit = {
    switchStyle(infoFont)
    drawCenterText(snakeGame, loc.stateMessagePause)
  }

  /**
    * Displays the game over message on top of the game.
    *
    * @param snakeGame the board canvas
    */
  def renderGameOver(snakeGame: AsnarcGameImpl): Unit = {
    switchStyle(gameOverFont)
    drawCenterText(snakeGame, loc.stateMessageGameOver)
  }

  /**
    * Draws a text in the middle of the game area. The text is written in the current style and color.
    *
    * @param game the board canvas on which the text is drawn
    * @param text      the text
    */
  def drawCenterText(game: AsnarcGame, text: String): Unit = {
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"
    renderer.fillText(text, game.state.board.cols * size / 2, game.state.board.rows * size / 2)
  }
}
