package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions.StateTransition

/**
  * An [[Entity]] that performs its update to the game world at a given game time step only.
  */
trait TimedEntity extends Entity {
  val time: Int

  def update(game: AsnarcWorld): Seq[StateTransition]
}
