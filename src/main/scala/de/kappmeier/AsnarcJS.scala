package de.kappmeier
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html

import Direction._

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
@JSExport
object AsnarcJS {
    var snakeGame: SnakeGameImpl = new SnakeGameImpl("0,0,┴")
    var asnarcState = STARTED

    /**
     * Initializes Asnarc for a new round.
     */
    def initGame(level: String): Unit = {
        snakeGame = new SnakeGameImpl(level)
        asnarcState = RUNNING
    }

    @JSExport
    def main(canvas: html.Canvas, level: String): Unit = {
        val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas)
        initGame(level)

        def run() = {
            if (asnarcState == RUNNING) {
                snakeGame.nextFrame()
                snakeGame.updateMove()
                if (snakeGame.dead) {
                    asnarcState = GAME_OVER
                }
            }

            renderer.render(snakeGame, asnarcState)
        }

        dom.window.setInterval(run _, 100)

        canvas.onclick = (e: dom.MouseEvent) => {
            asnarcState match {
                case STARTED => initGame("0,0,┴")
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
