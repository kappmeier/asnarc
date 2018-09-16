package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.SnakeGame

case class Death() extends StateTransition with WorldTransition {
  def updateWorld(game: SnakeGame) {
    game.state = game.state.copy(dead = true)
  }
}
