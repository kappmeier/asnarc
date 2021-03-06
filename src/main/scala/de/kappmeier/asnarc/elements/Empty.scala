package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point

import scala.collection.immutable.Set

object Empty extends Element {
  override def p = Point(-1, -1)

  override def connects = Set.empty[Direction]
}
