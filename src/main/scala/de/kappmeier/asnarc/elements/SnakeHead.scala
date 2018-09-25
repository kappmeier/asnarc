package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}

import scala.collection.immutable.Set

case class SnakeHead(p: Point, moveDirection: Direction, connects: Set[Direction]) extends Element {

  def this(x: Int, y: Int, moveDirection: Direction) {
    this(Point(x, y), moveDirection, Set.empty[Direction])
  }

  def this(p: Point, moveDirection: Direction) {
    this(p, moveDirection: Direction, Set[Direction](Direction.opposite(moveDirection)))
  }
}
