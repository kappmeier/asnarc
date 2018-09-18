package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{AsnarcGame, TimeConst}
import de.kappmeier.asnarc.transitions.StateTransition

/**
  * Timer until food disappears
  */
case class SpecialFoodDisappearPeriod(food: SpecialFood, time: Int) extends TimedEntity {

  def update(game: AsnarcGame): Seq[StateTransition] = {
    val potentialFood = game.board.elementAt(food.p)
    if (potentialFood == food) {
      val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
      game.board = game.board.removeElement(food.p)
      game.specialFood = None
      game.addTimer(SpecialFoodWaitPeriod(appearAt))
    }
    Seq.empty
  }
}
