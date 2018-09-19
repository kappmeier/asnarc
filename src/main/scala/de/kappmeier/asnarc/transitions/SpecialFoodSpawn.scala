package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld, TimeConst}

case class SpecialFoodSpawn() extends StateTransition with WorldTransition {
  /**
    * Creates a new entity for special food, places its element on the game board.
    *
    * @param game
    * @return
    */
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    val foodPosition = game.board.freeLocation()

    val disappearAt = game.time() + (TimeConst.TimeForSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
    val specialFood = SpecialFood(foodPosition, disappearAt)

    game.board = game.board.addElement(specialFood)
    game.entities = game.entities + (specialFood)

    game.state
  }
}
