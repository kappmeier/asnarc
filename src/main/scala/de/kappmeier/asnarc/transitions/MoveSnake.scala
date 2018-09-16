package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.SnakeGame

case class MoveSnake() extends StateTransition with WorldTransition {
  def updateWorld(game: SnakeGame) {
    if (game.snake.nonEmpty) {
      game.removeElement(game.snake.dequeue().p)
    }
  }
}
