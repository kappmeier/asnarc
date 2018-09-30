package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point

import scala.collection.immutable.Set

class SnakeElement(val p: Point, val connects: Set[Direction]) extends Element {
}

object SnakeElement
