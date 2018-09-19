package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Food
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class EatFood(f: Food) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame): AsnarcWorld = {
    val foodPosition = game.state.board.freeLocation()
    val newFood = Food(foodPosition)
    game.state = game.state.copy(board = game.state.board.addElement(newFood),
    entities = game.state.entities - f + newFood)
    game.state
  }
}
