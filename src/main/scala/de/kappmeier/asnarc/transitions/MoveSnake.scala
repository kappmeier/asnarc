package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.{SnakeBody, SnakeHead}
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

import scala.collection.immutable.Queue

case class MoveSnake(oldHead: SnakeHead) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame): AsnarcWorld = {
    if (game.state.player.body.nonEmpty) {

      val newBodyElement = new SnakeBody(oldHead, game.direction())
      val tempBody = game.state.player.body.enqueue(newBodyElement)

      val (lastSnakeElement, remainingElements): (SnakeBody, Queue[SnakeBody]) = tempBody.dequeue
      val newPlayer = new Player(game.state.player.head, remainingElements)

      game.state = game.state.copy(board = game.state.board.removeElement(lastSnakeElement.p).addElement(newBodyElement),
        entities = game.state.entities.-(game.state.player).+(newPlayer),
        player = newPlayer
      )
    }
    game.state
  }
}
