package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.SnakeGame

/**
  * Events that spawn new [[Entity]]s
  */
trait WorldTransition extends StateTransition {
  def updateWorld(game: SnakeGame)
}
