package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.SpecialFood
import de.kappmeier.asnarc.game.{AsnarcWorld, TimeConst}

case class SpecialFoodSpawn(timeExist: Int) extends StateTransition with WorldTransition {

  /**
    * Creates a new entity for special food, places its element on the game board.
    *
    * @param gameWorld
    * @return
    */
  override def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    val foodPosition = gameWorld.board.freeLocation()

    val disappearAt = gameWorld.time + timeExist
    val specialFood = SpecialFood(foodPosition, disappearAt)

    gameWorld.copy(board = gameWorld.board.addDynamicElement(specialFood), entities = gameWorld.entities + specialFood)
  }
}
