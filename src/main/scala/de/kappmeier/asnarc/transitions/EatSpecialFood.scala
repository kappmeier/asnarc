package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class EatSpecialFood(f: SpecialFood) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame): AsnarcWorld = {
    // Remove the food
    game.entities = game.entities - f

    game.state
  }
}
