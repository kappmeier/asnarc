package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class ReplaceEntity(toRemove: Entity, newEntity: Entity) extends StateTransition with WorldTransition {
  /**
    * Takes the previous world state and returns the new state.
    *
    * @param game
    * @return
    */
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    game.entities = game.entities.-(toRemove).+(newEntity)
    game.state
  }
}
