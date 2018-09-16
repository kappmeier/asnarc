package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.SnakeGame
import de.kappmeier.asnarc.transitions.StateTransition

trait TimedEntity {
  val time: Int

  def update(game: SnakeGame): Seq[StateTransition]
}
