package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.AsnarcWorld

/**
  * A transition that is able to update the game world.
  */
trait WorldTransition extends StateTransition {


  /**
    * Takes the previous world state and returns the new state.
    * @param gameWorld
    * @return
    */
  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld
}
