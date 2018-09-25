package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcState._
import de.kappmeier.asnarc.board.Direction
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom
import org.scalajs.dom.html

import scala.collection.immutable.HashMap

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

  def render(gameWorld: AsnarcWorld, asnarcState: AsnarcState) {
    renderer.clearRect(0, 0, canvas.width, canvas.height)

    asnarcState match {
      case Started => // start the game
        renderStart(gameWorld)
      case Running =>
        renderMove(gameWorld)
        renderInfo(gameWorld)
      case GameOver =>
        renderMove(gameWorld)
        renderGameOver(gameWorld)
      // restart the game
      case Pause => // continue the game
        renderMove(gameWorld)
        renderPause(gameWorld)
        renderInfo(gameWorld)
    }
  }

  def renderStart(gameWorld: AsnarcWorld): Unit = {
    val color = drawColors.getOrElse(Wall.getClass.getSimpleName, "black")
    renderer.fillStyle = color
    gameWorld.board.map.withFilter { tuple => tuple._2.isInstanceOf[Wall] }.foreach { tuple => fillElement(tuple._2) }

    switchStyle(infoFont)
    drawCenterText(gameWorld, loc.stateMessageStart)
  }

  def renderMove(gameWorld: AsnarcWorld): Unit = {
    // Draw background
    for {(_, e) <- gameWorld.board.map} {
      val color = drawColors.getOrElse(e.getClass.getSimpleName, "yellow")
      renderer.fillStyle = color
      fillElement(e)
    }
    renderer.fillStyle = drawColors(SnakeHead.getClass.getSimpleName)
    fillElement(gameWorld.player.head)
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
    * @param gameWorld the game canvas
    */
  def renderInfo(gameWorld: AsnarcWorld): Unit = {
    val len: Int = gameWorld.player.body.size + 1
    switchStyle(informationStyle)
    renderer.textAlign = "left"
    renderer.textBaseline = "top"
    val averageTime = gameWorld.time / len
    // TODO: replace default -1 with actual turn count
    val averageTurns = -1 / len
    val infoString = loc.statusText.format(len, 0, averageTime, averageTurns)
    renderer.fillText(infoString, border, gameWorld.board.rows * size + border)
    renderer.textAlign = "right"
    val locString = loc.statusPosition.format(gameWorld.player.head.p.x, gameWorld.player.head.p.y)
    renderer.fillText(locString, size * gameWorld.board.cols - border, gameWorld.board.rows * size + border)
  }

  /**
    * Displays the pause message on top of the game.
    *
    * @param gameWorld the board canvas
    */
  def renderPause(gameWorld: AsnarcWorld): Unit = {
    switchStyle(infoFont)
    drawCenterText(gameWorld, loc.stateMessagePause)
  }

  /**
    * Displays the game over message on top of the game.
    *
    * @param gameWorld the board canvas
    */
  def renderGameOver(gameWorld: AsnarcWorld): Unit = {
    switchStyle(gameOverFont)
    drawCenterText(gameWorld, loc.stateMessageGameOver)
  }

  /**
    * Draws a text in the middle of the game area. The text is written in the current style and color.
    *
    * @param gameWorld the board canvas on which the text is drawn
    * @param text      the text
    */
  def drawCenterText(gameWorld: AsnarcWorld, text: String): Unit = {
    renderer.textAlign = "center"
    renderer.textBaseline = "middle"
    renderer.fillText(text, gameWorld.board.cols * size / 2, gameWorld.board.rows * size / 2)
  }
}
