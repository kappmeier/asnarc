package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.AsnarcWorld

case class ReplaceEntity(toRemove: Entity, newEntity: Entity) extends StateTransition with WorldTransition {
  /**
    * Takes the previous world state and returns the new state.
    *
    * @param gameWorld
    * @return
    */
  override def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    gameWorld.copy(entities = gameWorld.entities - toRemove + newEntity)
  }
}
