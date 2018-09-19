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
    val foodPosition = game.state.board.freeLocation()

    val disappearAt = game.time() + (TimeConst.TimeForSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
    val specialFood = SpecialFood(foodPosition, disappearAt)

    game.state = game.state.copy(board = game.state.board.addElement(specialFood),
    entities = game.state.entities + specialFood)

    game.state
  }
}
