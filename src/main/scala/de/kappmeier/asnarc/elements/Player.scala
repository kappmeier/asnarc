package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.SnakeGame
import de.kappmeier.asnarc.transitions.StateTransition

import scala.collection.immutable.Set

case class Player(p: Point, connects: Set[Direction]) extends Entity with ActionElement {
  def this(x: Int, y: Int) {
    this(Point(x, y), Set.empty[Direction])
  }

  def this(p: Point, moveDirection: Direction) {
    this(p, Set[Direction](Direction.opposite(moveDirection)))
  }

  def update(game: SnakeGame): Seq[StateTransition] = Seq.empty
}
