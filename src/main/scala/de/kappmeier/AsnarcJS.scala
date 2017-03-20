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
    var snakeGame: SnakeGameImpl = new SnakeGameImpl
    var asnarcState = STARTED

    /**
     * Initializes Asnarc for a new round.
     */
    def initGame(): Unit = {
        snakeGame = new SnakeGameImpl
        asnarcState = RUNNING
    }

    @JSExport
    def main(canvas: html.Canvas): Unit = {
        val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas)

        def run() = {
            snakeGame.nextFrame()

            if (asnarcState == RUNNING) {
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
                case STARTED => initGame()
                case RUNNING => asnarcState = PAUSE
                case PAUSE => asnarcState = RUNNING
            }
        }

        canvas.onkeydown = (e: dom.KeyboardEvent) => {
            asnarcState match {
                case STARTED => initGame()
                case RUNNING =>
                    val newDirection: Option[DirectionVal] = Direction.byKeyCode(e.keyCode)
                    if (newDirection.isDefined) {
                        snakeGame.newDirection(newDirection.get)
                    }
                case GAME_OVER => initGame()
                case PAUSE => asnarcState = RUNNING
            }
        }
    }
}
