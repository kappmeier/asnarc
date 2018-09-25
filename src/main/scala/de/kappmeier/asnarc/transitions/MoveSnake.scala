package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.{SnakeBody, SnakeHead}
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

import scala.collection.immutable.Queue

case class MoveSnake(oldHead: SnakeHead) extends StateTransition with WorldTransition {
  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    if (gameWorld.player.body.nonEmpty) {

      val newBodyElement = new SnakeBody(oldHead, gameWorld.player.head.moveDirection)
      val tempBody = gameWorld.player.body.enqueue(newBodyElement)

      val (lastSnakeElement, remainingElements): (SnakeBody, Queue[SnakeBody]) = tempBody.dequeue
      val newPlayer = new Player(gameWorld.player.head, remainingElements)

      gameWorld.copy(board = gameWorld.board.removeElement(lastSnakeElement.p).addElement(newBodyElement),
        entities = gameWorld.entities.-(gameWorld.player).+(newPlayer), player = newPlayer
      )
    } else {
      gameWorld
    }
  }
}
