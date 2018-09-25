package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.elements.SnakeBody
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

import scala.collection.immutable.{Queue, Set}

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
  def this(player: Player) = this(player, false)

  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {

    val newElement: SnakeBody =
      if (full) SnakeBody(player.head.p, Set[Direction](Left, Right, Up, Down))
      else new SnakeBody(player.head, gameWorld.player.head.moveDirection)
    val newBody: Queue[SnakeBody] = gameWorld.player.body.enqueue(newElement)
    val newPlayer: Player = new Player(gameWorld.player.head, newBody)

    gameWorld.copy(board = gameWorld.board.addElement(newElement),
      entities = gameWorld.entities.-(gameWorld.player).+(newPlayer), player = newPlayer)
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}
