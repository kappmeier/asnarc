package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point

case class Teleport(p: Point, connects: Set[Direction]) extends Element
