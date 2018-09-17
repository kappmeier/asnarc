package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions.StateTransition

trait TimedEntity {
  val time: Int

  def update(game: AsnarcGame): Seq[StateTransition]
}
