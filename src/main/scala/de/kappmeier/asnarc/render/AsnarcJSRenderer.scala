package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcState._
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import de.kappmeier.asnarc.{Element, AsnarcState => _, _}
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
        Body.getClass.getSimpleName -> "darkgreen",
        SpecialFood.getClass.getSimpleName -> "orange",
        Player.getClass.getSimpleName -> "green"
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

    def render(snakeGame: SnakeGameImpl, asnarcState: AsnarcState) {
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

    def renderStart(snakeGame: SnakeGameImpl): Unit = {
        val color = drawColors.getOrElse(Wall.getClass.getSimpleName, "black")
        renderer.fillStyle = color
        snakeGame.map.withFilter { tuple => tuple._2.isInstanceOf[Wall] }.foreach { tuple => fillElement(tuple._2) }

        switchStyle(infoFont)
        drawCenterText(snakeGame, loc.stateMessageStart)
    }

    def renderMove(snakeGame: SnakeGameImpl): Unit = {
        // Draw background
        for ((_, e) <- snakeGame.map) {
            val color = drawColors.getOrElse(e.getClass.getSimpleName, "yellow")
            renderer.fillStyle = color
            fillElement(e)
        }
        renderer.fillStyle = drawColors(Player.getClass.getSimpleName)
        fillElement(snakeGame.state.player)
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
      * @param snakeGame the game canvas
      */
    def renderInfo(snakeGame: SnakeGameImpl): Unit = {
        val len: Int = snakeGame.snake.size + 1
        switchStyle(informationStyle)
        renderer.textAlign = "left"
        renderer.textBaseline = "top"
        val averageTime = snakeGame.frame / len
        val averageTurns = snakeGame.turns / len
        val infoString = loc.statusText.format(len, 0, averageTime, averageTurns)
        renderer.fillText(infoString, border, snakeGame.rows * size + border)
        renderer.textAlign = "right"
        val locString = loc.statusPosition.format(snakeGame.state.player.p.x, snakeGame.state.player.p.y)
        renderer.fillText(locString, size * snakeGame.cols - border, snakeGame.rows * size + border)
    }

    /**
      * Displays the pause message on top of the game.
      *
      * @param snakeGame the board canvas
      */
    def renderPause(snakeGame: SnakeGameImpl): Unit = {
        switchStyle(infoFont)
        drawCenterText(snakeGame, loc.stateMessagePause)
    }

    /**
      * Displays the game over message on top of the game.
      *
      * @param snakeGame the board canvas
      */
    def renderGameOver(snakeGame: SnakeGameImpl): Unit = {
        switchStyle(gameOverFont)
        drawCenterText(snakeGame, loc.stateMessageGameOver)
    }

    /**
      * Draws a text in the middle of the game area. The text is written in the current style and color.
      *
      * @param snakeGame the board canvas on which the text is drawn
      * @param text the text
      */
    def drawCenterText(snakeGame: SnakeGameImpl, text: String): Unit = {
        renderer.textAlign = "center"
        renderer.textBaseline = "middle"
        renderer.fillText(text, snakeGame.cols * size / 2, snakeGame.rows * size / 2)
    }
}
