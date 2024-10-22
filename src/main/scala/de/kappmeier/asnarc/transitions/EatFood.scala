package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Food
import de.kappmeier.asnarc.game.AsnarcWorld

case class EatFood(f: Food) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcWorld): AsnarcWorld = {
    val foodPosition = game.board.freeLocation()
    val newFood = Food(foodPosition)
    game.copy(board = game.board.addDynamicElement(newFood), entities = game.entities - f + newFood)
  }
}
