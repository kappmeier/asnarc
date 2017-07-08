package de.kappmeier.asnarc

import de.kappmeier.asnarc.Direction.Direction
import de.kappmeier.asnarc.render.AsnarcJSRenderer
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import org.scalajs.dom.html

/**
 * Different game states to render.
 */
object AsnarcState extends Enumeration {
    type AsnarcState = Value
    val Running, Started, GameOver, Pause = Value
}

/**
 * The ScalaJS export for Asnarc. Runs the game loop, grabs character input and renders the game.
 *
 * The visualization consists of a single line of status information and a rectangular field.
 */
@JSExportTopLevel("AsnarcJS")
object AsnarcJS {
    var asnarcState = AsnarcState.Started
    var snakeGame: SnakeGameImpl = new SnakeGameImpl(LevelGenerator.EmptyLevel)

    /**
     * Initializes Asnarc for a new round.
     */
    def initGame(level: String): Unit = {
        snakeGame = new SnakeGameImpl(level)
        asnarcState = AsnarcState.Running
    }

    @JSExport
    def main(canvas: html.Canvas, level: String): Unit = {
        val localization = new AsnarcLocalizationDe
        val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas, localization)
        initGame(level)

        scala.scalajs.js.timers.setInterval(100) {
            if (asnarcState == AsnarcState.Running) {
                snakeGame.nextFrame()
                snakeGame.updateMove()
                if (snakeGame.state.dead) {
                    asnarcState = AsnarcState.GameOver
                }
            }

            renderer.render(snakeGame, asnarcState)
        }

        canvas.onclick = (e: dom.MouseEvent) => {
            asnarcState match {
                case AsnarcState.Started => initGame(LevelGenerator.EmptyLevel)
                case AsnarcState.Running => asnarcState = AsnarcState.Pause
                case AsnarcState.Pause => asnarcState = AsnarcState.Running
                case AsnarcState.GameOver =>
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
                case AsnarcState.Started => initGame(level)
                case AsnarcState.Running =>
                    updateDirection(e.keyCode)

                    if (pauseKeyCodes.contains(e.keyCode)) {
                        asnarcState = AsnarcState.Pause
                    }
                case AsnarcState.GameOver => initGame(level)
                case AsnarcState.Pause =>
                    updateDirection(e.keyCode)
                    asnarcState = AsnarcState.Running
            }
        }
    }
}
