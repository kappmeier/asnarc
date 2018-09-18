package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}

import scala.collection.immutable.Set

case class SnakeHead(p: Point, connects: Set[Direction]) extends Element {
  def this(x: Int, y: Int) {
    this(Point(x, y), Set.empty[Direction])
  }

  def this(p: Point, moveDirection: Direction) {
    this(p, Set[Direction](Direction.opposite(moveDirection)))
  }
}
