package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.SnakeGame
import de.kappmeier.asnarc.transitions.{Death, StateTransition}

import scala.collection.immutable.Set

case class Body(p: Point, connects: Set[Direction]) extends Entity with StaticElement {
  def this(e: Player, connectsAlso: Direction) {
    this(e.p, e.connects + connectsAlso)
  }

  def update(game: SnakeGame): Seq[StateTransition] = Seq(Death())
}
