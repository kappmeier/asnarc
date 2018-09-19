package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class Death() extends StateTransition with WorldTransition {
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    game.state = game.state.copy(dead = true)
    game.state
  }
}
