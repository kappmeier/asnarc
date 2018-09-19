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
    game.state = game.state.copy(entities = game.state.entities - toRemove)
    for (element <- elements) {
      game.state = game.state.copy(board = game.state.board.removeElement(element.p))
    }
    game.state
  }
}
