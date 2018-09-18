package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{AsnarcGame, TimeConst}
import de.kappmeier.asnarc.transitions.StateTransition

case class SpecialFoodWaitPeriod(time: Int) extends TimedEntity {
  /**
    * Creates a new timer to create food.
    */
  def update(game: AsnarcGame): Seq[StateTransition] = {
    val foodPosition = game.board.freeLocation()
    game.specialFood = Some(foodPosition)
    val specialFood = SpecialFood(foodPosition)

    val disappearAt = game.time() + (TimeConst.TimeForSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]

    // timer to disappear
    val timer = SpecialFoodDisappearPeriod(specialFood, disappearAt)

    game.board = game.board.addElement(foodPosition, specialFood)

    game.addTimer(timer)
    Seq.empty
  }
}
