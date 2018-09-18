package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.Set
import scala.collection.mutable

class Player(p: Point, connects: Set[Direction]) extends Entity {
  def this(x: Int, y: Int, connects: Set[Direction]) {
    this(Point(x, y), connects)
  }

  def this(p: Point, moveDirection: Direction) {
    this(p, Set[Direction](Direction.opposite(moveDirection)))
  }

  val body: mutable.Queue[SnakeBody] = new mutable.Queue[SnakeBody]()

  val head: SnakeHead = new SnakeHead(p, connects)

  /**
    * When the field is empty, the snake moves on. When there is a wall or an element of the snake itself, it
    * dies.
    *
    * @param game the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(game: AsnarcGame): Seq[StateTransition] = {
    val elementBelow: Element = game.board.elementAt(head.p)

    elementBelow match {
      case _: Wall => Seq(Death())
      case _: SnakeBody => Seq(Death())
      case _: Food => Seq(MoveHead(game.direction()), AppendSnake(game.state.player))
      case _ => Seq(MoveHead(game.direction()), AppendSnake(game.state.player), MoveSnake())
    }
  }
}
