package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Food
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class EatFood(f: Food) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame): AsnarcWorld = {
    game.entities = game.entities - f
    val foodPosition = game.board.freeLocation()
    val newFood = Food(foodPosition)
    game.board = game.board.addElement(newFood)
    game.entities = game.entities + newFood
    game.state
  }
}
