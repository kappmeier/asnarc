package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.elements.{Body, Player}
import de.kappmeier.asnarc.game.AsnarcGame

import scala.collection.immutable.Set

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
  def this(player: Player) = this(player, false)

  def updateWorld(game: AsnarcGame) {

    val newElement: Body = if (full) Body(player.p, Set[Direction](Left, Right, Up, Down)) else new Body(player, game.direction())
    game.player.enqueue(newElement)
    game.board = game.board.addElement(player.p, newElement)
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}
