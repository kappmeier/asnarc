package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.elements.{Body, Player}
import de.kappmeier.asnarc.game.SnakeGame

import scala.collection.immutable.Set

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
  def this(player: Player) = this(player, false)

  def updateWorld(game: SnakeGame) {

    val newElement: Body = if (full) Body(player.p, Set[Direction](Left, Right, Up, Down)) else new Body(player, game.direction())
    game.snake.enqueue(newElement)
    game.addElement(player.p, newElement)
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}
