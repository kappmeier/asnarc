package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

case class AppendSnake(full: Boolean = false) extends StateTransition with WorldTransition {

  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    val newPlayerPos: Point = Player.nextPos(gameWorld)
    val newPlayer: Player = gameWorld.player.extend(newPlayerPos, full)
    gameWorld.copy(
      board = gameWorld.board.addDynamicElement(newPlayer.snakeHead()).removeDynamicElement(gameWorld.player.snakeHead()).addDynamicElement(newPlayer.elementAt(1)),
      entities = gameWorld.entities - gameWorld.player + newPlayer,
      player = newPlayer
      )
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply() = new AppendSnake()
}
