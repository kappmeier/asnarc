package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.AsnarcWorld

case class Death() extends StateTransition with WorldTransition {
  override def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    gameWorld.copy(dead = true)
  }
}
