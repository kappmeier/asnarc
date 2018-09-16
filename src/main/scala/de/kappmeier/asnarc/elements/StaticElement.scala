package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.SnakeGame
import de.kappmeier.asnarc.transitions.StateTransition

/**
  * Passive elements.
  */
trait StaticElement extends Entity with Element {
  def update(game: SnakeGame): Seq[StateTransition]
}
