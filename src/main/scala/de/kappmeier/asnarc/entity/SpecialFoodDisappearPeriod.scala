package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{SnakeGame, TimeConst}
import de.kappmeier.asnarc.transitions.StateTransition

/**
  * Timer until food disappears
  */
case class SpecialFoodDisappearPeriod(food: SpecialFood, time: Int) extends TimedEntity {

  def update(game: SnakeGame): Seq[StateTransition] = {
    val potentialFood = game.elementAt(food.p)
    if (potentialFood == food) {
      val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
      game.removeElement(food.p)
      game.addTimer(SpecialFoodWaitPeriod(appearAt))
    }
    Seq.empty
  }
}
