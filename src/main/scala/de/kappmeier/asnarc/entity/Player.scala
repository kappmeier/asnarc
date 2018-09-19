package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.{Queue, Set}

class Player(val head: SnakeHead, val body: Queue[SnakeBody]) extends Entity {
  def this(p: Point, connects: Set[Direction]) {
    this(new SnakeHead(p, connects), Queue.empty)
  }

  def this(p: Point, moveDirection: Direction, body: Queue[SnakeBody]) {
    this(new SnakeHead(p, Set[Direction](Direction.opposite(moveDirection))), body)
  }

  def this(x: Int, y: Int, connects: Set[Direction]) {
    this(Point(x, y), connects)
  }

  def this(p: Point, moveDirection: Direction) {
    this(p, Set[Direction](Direction.opposite(moveDirection)))
  }

  /**
    * When the field is empty, the snake moves on. When there is a wall or an element of the snake itself, it
    * dies.
    *
    * @param game the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(game: AsnarcGame): Seq[StateTransition] = {
    val nextElement: Element = game.state.board.elementAt(Player.nextPos(game))

    nextElement match {
      case _: Wall => Seq(Death())
      case _: SnakeBody => Seq(Death())
      case _: Food => Seq(MoveHead(game.direction()), AppendSnake(game.state.player))
      case _: SpecialFood => Seq(MoveHead(game.direction()), AppendSnake(game.state.player, full = true))
      case _ => Seq(MoveHead(game.direction()), MoveSnake(game.state.player.head))
    }
  }


  override def toString = s"Player($head, $body)"
}

object Player {
  def nextPos(game: AsnarcGame): Point = {
    (game.state.player.head.p + game.direction().direction) % (60, 40)
  }

}