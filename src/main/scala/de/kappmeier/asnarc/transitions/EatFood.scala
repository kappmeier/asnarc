package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Food
import de.kappmeier.asnarc.game.AsnarcGame

case class EatFood(f: Food) extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame) {
    game.board = game.board.removeElement(f.p)
    val foodPosition = game.board.freeLocation()
    game.board = game.board.addElement(foodPosition, Food(foodPosition))
    game.initialFood = foodPosition
  }
}
