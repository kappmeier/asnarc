package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions.StateTransition

/**
  * Any entity in the Asnarc game. An entity is asked in each game loop iteration to check if the game needs to be
  * updated. If this is the case, it emits a list of [[StateTransition]]s that will be executed afterwards.
  */
trait Entity {
  /**
    * Decides how the game world should be updated.
    *
    * @param gameWorld the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  def update(gameWorld: AsnarcWorld): Seq[StateTransition]

}