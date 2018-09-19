package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Element
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class RemoveEntity(toRemove: Entity, elements: Seq[Element]) extends StateTransition with WorldTransition {
  /**
    * Takes the previous world state and returns the new state.
    *
    * @param game
    * @return
    */
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    game.entities = game.entities - toRemove
    for (element <- elements) {
      game.board = game.board.removeElement(element.p)
    }
    game.state
  }
}
