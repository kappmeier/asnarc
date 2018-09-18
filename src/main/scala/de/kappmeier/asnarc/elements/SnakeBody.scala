package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point

import scala.collection.immutable.Set

case class SnakeBody(p: Point, connects: Set[Direction]) extends Element {
  def this(e: SnakeHead, connectsAlso: Direction) {
    this(e.p, e.connects + connectsAlso)
  }
}
