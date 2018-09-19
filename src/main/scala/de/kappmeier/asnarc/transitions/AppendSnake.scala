package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.elements.SnakeBody
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

import scala.collection.immutable.{Queue, Set}

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
  def this(player: Player) = this(player, false)

  def updateWorld(game: AsnarcGame): AsnarcWorld = {

    val newElement: SnakeBody =
      if (full) SnakeBody(player.head.p, Set[Direction](Left, Right, Up, Down))
      else new SnakeBody(player.head, game.direction())
    val newBody: Queue[SnakeBody] = game.state.player.body.enqueue(newElement)
    val newPlayer: Player = new Player(game.state.player.head, newBody)

    game.state = game.state.copy(board = game.state.board.addElement(newElement),
      entities = game.state.entities.-(game.state.player).+(newPlayer),
      player = newPlayer)

    game.state
  }
}

/**
  * Companion object.
  */
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}
