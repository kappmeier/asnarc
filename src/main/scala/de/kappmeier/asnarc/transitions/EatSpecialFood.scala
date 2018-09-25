package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.AsnarcWorld

case class EatSpecialFood(f: SpecialFood) extends StateTransition with WorldTransition {
  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    // Remove the food
    gameWorld.copy(entities = gameWorld.entities - f)
  }
}
