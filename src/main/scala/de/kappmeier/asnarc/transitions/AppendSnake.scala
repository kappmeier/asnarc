package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.elements.SnakeBody
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcGame

import scala.collection.immutable.Set

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
  def this(player: Player) = this(player, false)

  def updateWorld(game: AsnarcGame) {

    val newElement: SnakeBody =
      if (full) SnakeBody(player.head.p, Set[Direction](Left, Right, Up, Down))
      else new SnakeBody(player.head, game.direction())
    game.player.body.enqueue(newElement)
    game.board = game.board.addElement(player.head.p, newElement)
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}
