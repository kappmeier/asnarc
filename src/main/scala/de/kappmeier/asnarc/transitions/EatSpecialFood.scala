package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.entity.SpecialFoodWaitPeriod
import de.kappmeier.asnarc.game.{AsnarcGame, TimeConst}

case class EatSpecialFood(f: SpecialFood) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame) {
    // Remove the food
    game.board = game.board.removeElement(f.p)

    // Place a new timer
    val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
    game.addTimer(SpecialFoodWaitPeriod(appearAt))
    game.specialFood = None
  }
}
