package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point

trait Element {
  def p: Point

  def connects: Set[Direction]
}
