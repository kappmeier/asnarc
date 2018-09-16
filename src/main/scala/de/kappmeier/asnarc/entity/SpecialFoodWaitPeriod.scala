package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{SnakeGame, TimeConst}
import de.kappmeier.asnarc.transitions.StateTransition

case class SpecialFoodWaitPeriod(time: Int) extends TimedEntity {
  /**
    * Creates a new timer to create food.
    */
  def update(game: SnakeGame): Seq[StateTransition] = {
    val foodPosition = game.freeLocation()
    val specialFood = SpecialFood(foodPosition)

    val disappearAt = game.time() + (TimeConst.TimeForSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]

    // timer to disappear
    val timer = SpecialFoodDisappearPeriod(specialFood, disappearAt)

    game.addElement(foodPosition, specialFood)

    game.addTimer(timer)
    Seq.empty
  }
}
