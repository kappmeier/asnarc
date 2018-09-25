package de.kappmeier.asnarc

import de.kappmeier.asnarc.board.Direction
import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.game.{AsnarcGameImpl, AsnarcWorld}
import de.kappmeier.asnarc.render.AsnarcJSRenderer
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import org.scalajs.dom
import org.scalajs.dom.html

import scala.collection.mutable

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

  @JSExport
  def main(canvas: html.Canvas, level: String): Unit = {
    val asnarcGame: AsnarcGameImpl = new AsnarcGameImpl(level)

    var asnarcState = AsnarcState.Running
    var gameWorld: AsnarcWorld = asnarcGame.initGameWorld()

    val keys = new mutable.Queue[Direction]
    var turns = 0

  /**
    * Initializes Asnarc for a new round.
    */
  def initGame(): Unit = {
    gameWorld = asnarcGame.initGameWorld()
    asnarcState = AsnarcState.Running
  }

    val localization = new AsnarcLocalizationDe
    val renderer: AsnarcJSRenderer = new AsnarcJSRenderer(canvas, localization)
    initGame()

    scala.scalajs.js.timers.setInterval(100) {
      if (asnarcState == AsnarcState.Running) {
        gameWorld = asnarcGame.nextFrame(gameWorld)

        if (keys.nonEmpty) {
          gameWorld = asnarcGame.newDirection(gameWorld, keys.dequeue)
          turns += 1
        }

        gameWorld = asnarcGame.updateMove(gameWorld)
        if (gameWorld.dead) {
          asnarcState = AsnarcState.GameOver
        }
      }

      renderer.render(gameWorld, asnarcState)
    }

    canvas.onclick = (e: dom.MouseEvent) => {
      asnarcState match {
        case AsnarcState.Started => initGame()
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
          keys.enqueue(newDirection.get)
        }
      }

      asnarcState match {
        case AsnarcState.Started => initGame()
        case AsnarcState.Running =>
          updateDirection(e.keyCode)

          if (pauseKeyCodes.contains(e.keyCode)) {
            asnarcState = AsnarcState.Pause
          }
        case AsnarcState.GameOver => initGame()
        case AsnarcState.Pause =>
          updateDirection(e.keyCode)
          asnarcState = AsnarcState.Running
      }
    }
  }
}
