package de.kappmeier.asnarc

import de.kappmeier.asnarc.Direction.Direction
import de.kappmeier.asnarc.render.AsnarcJSRenderer
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe

import scala.scalajs.js.annotation.JSExport
import scala.scalajs.js.annotation.JSExportTopLevel
import org.scalajs.dom
import org.scalajs.dom.html

/**
 * Different game states to render.
 */
object AsnarcState extends Enumeration {
    type AsnarcState = Value
    val RUNNING, STARTED, GAME_OVER, PAUSE = Value
}
import AsnarcState._

/**
 * The ScalaJS export for Asnarc. Runs the game loop, grabs character input and renders the game.
 *
 * The visualization consists of a single line of status information and a rectangular field.
 */
@JSExportTopLevel("AsnarcJS")
object AsnarcJS {
    var asnarcState = STARTED
    var snakeGame: SnakeGameImpl = new SnakeGameImpl("0,0, ")

    /**
     * Initializes Asnarc for a new round.
     */
    def initGame(level: String): Unit = {
        snakeGame = new SnakeGameImpl(level)
        asnarcState = RUNNING
    }

    @JSExport
    def main(canvas: html.Canvas, level: String): Unit = {
        val localization = new AsnarcLocalizationDe
        val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas, localization)
        initGame(level)

        scala.scalajs.js.timers.setInterval(100) {
            if (asnarcState == RUNNING) {
                snakeGame.nextFrame()
                snakeGame.updateMove()
                if (snakeGame.state.dead) {
                    asnarcState = GAME_OVER
                }
            }

            renderer.render(snakeGame, asnarcState)
        }

        canvas.onclick = (e: dom.MouseEvent) => {
            asnarcState match {
                case STARTED => initGame("0,0, ")
                case RUNNING => asnarcState = PAUSE
                case PAUSE => asnarcState = RUNNING
                case GAME_OVER =>
            }
        }

        val pauseKeyCodes = Set(13, 32)
        canvas.onkeydown = (e: dom.KeyboardEvent) => {
            def updateDirection(keyCode: Int): Unit = {
                val newDirection: Option[Direction] = Direction.byKeyCode(e.keyCode)
                if (newDirection.isDefined) {
                    snakeGame.newDirection(newDirection.get)
                }
            }
            asnarcState match {
                case STARTED => initGame(level)
                case RUNNING =>
                    updateDirection(e.keyCode)

                    if (pauseKeyCodes.contains(e.keyCode)) {
                        asnarcState = PAUSE
                    }
                case GAME_OVER => initGame(level)
                case PAUSE =>
                    updateDirection(e.keyCode)
                    asnarcState = RUNNING
            }
        }
    }
}
