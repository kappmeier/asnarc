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
  val asnarcState = STARTED

    /**
     * Initializes Asnarc for a new round.
     */
    def initGame(): Unit = {
        snakeGame = new SnakeGameImpl
    }

    @JSExport
    def main(canvas: html.Canvas): Unit = {
        val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas)

        def run() = {
            snakeGame.nextFrame()

            renderer.render(snakeGame)
        }

        dom.window.setInterval(run _, 100)

        canvas.onclick = (e: dom.MouseEvent) => {
            dom.console.log(e)
        }

        canvas.onkeydown = (e: dom.KeyboardEvent) => {
            if (snakeGame.dead) {
                initGame()
            } else {
                val newDirection: Option[DirectionVal] = Direction.byKeyCode(e.keyCode)
                if (newDirection.isDefined) {
                    snakeGame.newDirection(newDirection.get)
                }
            }
        }
    }
}
