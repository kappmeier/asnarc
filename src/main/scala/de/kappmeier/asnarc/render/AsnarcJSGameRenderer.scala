package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.AsnarcState._
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.render.localization.AsnarcLocalization
import org.scalajs.dom.html

/**
  * Draws the Asnarc game into a canvas.
  */
class AsnarcJSGameRenderer(canvas: html.Canvas, loc: AsnarcLocalization) extends AbstractAsnarcJSRenderer(canvas, loc) {

    val a: Class[_] = Food.getClass

    val gameOverFont: RendererTextStyle = RendererTextStyle("sans-serif", 20, "darkred")

    canvas.width = canvas.parentElement.clientWidth
    canvas.height = canvas.parentElement.clientHeight

    def render(gameWorld: AsnarcWorld, asnarcState: AsnarcState) {
        clear()

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
        val color = AsnarcJSRenderer.DrawColors.getOrElse(Wall.getClass.getSimpleName, "black")
        renderer.fillStyle = color
        gameWorld.board.map.withFilter { tuple => tuple._2.isInstanceOf[Wall] }.foreach { tuple => fillElement(tuple._2) }

        switchStyle(AsnarcJSRenderer.InfoFont)
        drawCenterText(gameWorld, loc.stateMessageStart)
    }

    def renderMove(gameWorld: AsnarcWorld): Unit = {
        // Draw background
      drawBoard(gameWorld.board)
    }

    /**
      * Displays the status message below the game canvas. The status contains some statistics (current length, points,
      * average turns, ...) and the current position of the head.
      *
      * @param gameWorld the game canvas
      */
    def renderInfo(gameWorld: AsnarcWorld): Unit = {
        val len: Int = gameWorld.player.length()
        val averageTime = gameWorld.time / len
        // TODO: replace default -1 with actual turn count
        val averageTurns = -1 / len
        val infoString = loc.statusText.format(len, 0, averageTime, averageTurns)
        val locString = loc.statusPosition.format(gameWorld.player.snakeHead().p.x, gameWorld.player.snakeHead().p.y)
        renderInfo(gameWorld.board, infoString, locString)
    }

    /**
      * Displays the pause message on top of the game.
      *
      * @param gameWorld the board canvas
      */
    def renderPause(gameWorld: AsnarcWorld): Unit = {
        switchStyle(AsnarcJSRenderer.InfoFont)
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
        renderer.fillText(text, gameWorld.board.cols * AsnarcJSRenderer.Size / 2, gameWorld.board.rows * AsnarcJSRenderer.Size / 2)
    }
}
