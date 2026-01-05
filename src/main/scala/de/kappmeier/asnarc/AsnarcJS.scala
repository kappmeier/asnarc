package de.kappmeier.asnarc

import scala.collection.mutable
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}
import scala.scalajs.js.timers.SetIntervalHandle

import org.scalajs.dom
import org.scalajs.dom.html

import de.kappmeier.asnarc.board.Direction
import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.game.{AsnarcGameImpl, AsnarcWorld}
import de.kappmeier.asnarc.levels.PredefinedLevels
import de.kappmeier.asnarc.render.AsnarcJSGameRenderer
import de.kappmeier.asnarc.render.AsnarcJSRenderer
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe

/**
  * Different game states to render.
  *
  * The level parameter can be either:
  * - A named level (e.g., "Empty", see [[PredefinedLevels]] for available levels)
  * - A raw Base64 encoded level string
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

  /**
    * The current interval handle for the game loop. Needs to be stopped when a new game is loaded (by [[main]]).
    */
  private var currentIntervalHandle: Option[SetIntervalHandle] = None

  @JSExport
  def main(canvas: html.Canvas, level: String, blockSize: Int = 10): Unit = {
    cleanUp()

    val resolvedLevel: String = PredefinedLevels.resolve(level) match {
      case Some(decodedLevel) =>
        dom.console.log(s"Using named level: $level")
        decodedLevel
      case None =>
        dom.console.log(s"Using custom level data")
        level
    }

    println("Base64 encoded level: '" + resolvedLevel + "'")

    val asnarcGame: AsnarcGameImpl = new AsnarcGameImpl(resolvedLevel)

    var asnarcState = AsnarcState.Running
    var gameWorld: AsnarcWorld = asnarcGame.initGameWorld()

    // Create renderer configuration with the specified block size
    val rendererConfig = new AsnarcJSRenderer(blockSize)

    // Adjust canvas size to fit the level
    canvas.width = rendererConfig.canvasWidth(gameWorld.board.cols)
    canvas.height = rendererConfig.canvasHeight(gameWorld.board.rows)

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
    val renderer: AsnarcJSGameRenderer = new AsnarcJSGameRenderer(canvas, localization, rendererConfig)
    initGame()

    currentIntervalHandle = Some(scala.scalajs.js.timers.setInterval(100) {
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
    })

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

  /**
    * Cleans up potentially existing status from previous game runs. That is, calls to [[main]] from JavaScript.
    *
    * Cleaned up resources:
    * - Game loop interval
    */
  private def cleanUp(): Unit = {
    currentIntervalHandle.foreach(scala.scalajs.js.timers.clearInterval)
  }
}
