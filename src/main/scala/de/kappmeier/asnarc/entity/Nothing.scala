package de.kappmeier.asnarc.entity
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions.StateTransition

object Nothing extends Entity {
  /**
    * Decides how the game world should be updated.
    *
    * @param game the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(game: AsnarcWorld): Seq[StateTransition] = Nil
}
