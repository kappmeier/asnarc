package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.Queue

class Player(val head: SnakeHead, val body: Queue[SnakeBody]) extends Entity {
  def this(p: Point, moveDirection: Direction) {
    this(new SnakeHead(p, moveDirection), Queue.empty)
  }

  def this(p: Point, moveDirection: Direction, body: Queue[SnakeBody]) {
    this(new SnakeHead(p, moveDirection), body)
  }

  def this(x: Int, y: Int, moveDirection: Direction) {
    this(Point(x, y), moveDirection)
  }

  /**
    * When the field is empty, the snake moves on. When there is a wall or an element of the snake itself, it
    * dies.
    *
    * @param gameWorld the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
    val nextElement: Element = gameWorld.board.elementAt(Player.nextPos(gameWorld))

    nextElement match {
      case _: Wall => Seq(Death())
      case _: SnakeBody => Seq(Death())
      case _: Food => Seq(MoveHead(gameWorld.player.head.moveDirection), AppendSnake(gameWorld.player))
      case _: SpecialFood => Seq(MoveHead(gameWorld.player.head.moveDirection), AppendSnake(gameWorld.player, full = true))
      case _ => Seq(MoveHead(gameWorld.player.head.moveDirection), MoveSnake(gameWorld.player.head))
    }
  }


  override def toString = s"Player($head, $body)"
}

object Player {
  def nextPos(gameWorld: AsnarcWorld): Point = {
    (gameWorld.player.head.p + gameWorld.player.head.moveDirection.direction) % (60, 40)
  }

}