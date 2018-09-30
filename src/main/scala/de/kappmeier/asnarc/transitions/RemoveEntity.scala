package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Element
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.AsnarcWorld

case class RemoveEntity(toRemove: Entity, elements: Seq[Element]) extends StateTransition with WorldTransition {
  /**
    * Takes the previous world state and returns the new state.
    *
    * @param gameWorld
    * @return
    */
  override def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    var intermediateWorld = gameWorld.copy(entities = gameWorld.entities - toRemove)
    for {element <- elements} {
      intermediateWorld = intermediateWorld.copy(board = intermediateWorld.board.removeElement(element))
    }
    intermediateWorld
  }
}
