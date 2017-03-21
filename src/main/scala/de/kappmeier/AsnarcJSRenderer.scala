package de.kappmeier

import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, html}

import scala.collection.immutable.HashMap
import AsnarcState._

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
class AsnarcJSRenderer(canvas: html.Canvas) {
    val drawColors: HashMap[String, String] = collection.immutable.HashMap[String, String](
        Food.getClass.getSimpleName -> "darkred",
        Wall.getClass.getSimpleName -> "black",
        Body.getClass.getSimpleName -> "darkgreen",
        SpecialFood.getClass.getSimpleName -> "orange",
        Player.getClass.getSimpleName -> "green"
    )

    val renderer: CanvasRenderingContext2D = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

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

    var counter: Int = 1

    def render(snakeGame: SnakeGameImpl, asnarcState: AsnarcState) {
        renderer.clearRect(0, 0, canvas.width, canvas.height)

        asnarcState match {
            case STARTED => // start the game
                renderStart(snakeGame)
            case RUNNING =>
                renderMove(snakeGame)
                renderInfo(snakeGame)
            case GAME_OVER =>
                renderMove(snakeGame)
                renderDead(snakeGame)
            // restart the game
            case PAUSE => // continue the game
                renderMove(snakeGame)
                renderPause(snakeGame)
                renderInfo(snakeGame)
        }
    }

    def drawCenterText(snakeGame: SnakeGameImpl, text: String): Unit = {
        renderer.textAlign = "center"
        renderer.textBaseline = "middle"
        renderer.fillText(text, snakeGame.cols * size / 2, snakeGame.rows * size / 2)
    }

    def renderStart(snakeGame: SnakeGameImpl): Unit = {
        val color = drawColors.getOrElse(Wall.getClass.getSimpleName, "black")
        renderer.fillStyle = color
        snakeGame.map.withFilter{ tuple => tuple._2.isInstanceOf[Wall] }.foreach{ tuple => fillElement(tuple._2) }

        switchStyle(infoFont)
        drawCenterText(snakeGame, "Press button or click to start")
    }

    def renderMove(snakeGame: SnakeGameImpl): Unit = {
        // Draw background
        for ((_, e) <- snakeGame.map) {
            val color = drawColors.getOrElse(e.getClass.getSimpleName, "yellow")
            renderer.fillStyle = color
            fillElement(e)
        }
        renderer.fillStyle = drawColors(Player.getClass.getSimpleName)
        fillElement(snakeGame.player)
    }

    def fillField(location: Point): Unit = {
        val x = location.x * size
        val y = location.y * size
        renderer.fillRect(x + border, y + border, drawSize, drawSize)
    }

    def fillFull(location: Point): Unit = {
        val x = location.x * size
        val y = location.y * size
        renderer.fillRect(x, y, size, size)
    }

    def renderInfo(snakeGame: SnakeGameImpl): Unit = {
        val len: Int = snakeGame.snake.size + 1
        switchStyle(informationStyle)
        renderer.textAlign = "left"
        renderer.textBaseline = "top"
        val formatString = "Länge: %d Punkte: %d Zeit pro Nahrung: %d Kurven pro Nahrung: %d"
        val averageTime = snakeGame.frame / len
        val averageTurns = snakeGame.turns / len
        val infoString = formatString.format(len, 0, averageTime, averageTurns)
        renderer.fillText(infoString, border, snakeGame.rows * size + border)
        renderer.textAlign = "right"
        val locString = "Position: (%d,%d)".format(snakeGame.player.p.x, snakeGame.player.p.y)
        renderer.fillText(locString, size * snakeGame.cols - border, snakeGame.rows * size + border)
    }

    def fillElement(location: Element): Unit = {
        val x = if (location.connects contains Direction.LEFT) location.p.x * size else location.p.x * size + border
        val y = if (location.connects contains Direction.UP) location.p.y * size else location.p.y * size + border
        val w = drawSize + (if (location.connects contains Direction.LEFT) 1 else 0) + (if (location.connects contains Direction.RIGHT) 1 else 0)
        val h = drawSize + (if (location.connects contains Direction.UP) 1 else 0) + (if (location.connects contains Direction.DOWN) 1 else 0)
        renderer.fillRect(x, y, w, h)
    }

    def renderDead(snakeGame: SnakeGameImpl): Unit = {
        switchStyle(gameOverFont)
        drawCenterText(snakeGame, "Game Over")
    }

    def renderPause(snakeGame: SnakeGameImpl): Unit = {
        switchStyle(infoFont)
        drawCenterText(snakeGame, "Pause")
    }

    def renderGameOver(snakeGame: SnakeGameImpl): Unit = {

    }
}
