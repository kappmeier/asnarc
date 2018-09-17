package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.AsnarcGame

case class Death() extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame) {
    game.state = game.state.copy(dead = true)
  }
}
