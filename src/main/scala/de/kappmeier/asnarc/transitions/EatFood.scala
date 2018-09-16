package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.elements.Food
import de.kappmeier.asnarc.game.SnakeGame

case class EatFood(f: Food) extends StateTransition with WorldTransition {
  def updateWorld(game: SnakeGame) {
    game.removeElement(f.p)
    val foodPosition = game.freeLocation()
    game.addElement(foodPosition, Food(foodPosition))
  }
}
