package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions.{ReplaceEntity, SpecialFoodSpawn, StateTransition}

class SpecialFoodSpawnTimer(val time: Int, timeBetween: Int, timeExist: Int) extends TimedEntity {


  /**
    * When the spawn time is reached, the timer emits some state transitions.
    *
    * It creates a new entity (special food) that is placed somewhere and exchanges itself with a new spawn timer.
    *
    * @param gameWorld
    * @return
    */
  override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
    if (gameWorld.time == time) {
      val appearAt = gameWorld.time + timeBetween
      Seq(new SpecialFoodSpawn(timeExist), new ReplaceEntity(this, new SpecialFoodSpawnTimer(appearAt, timeBetween, timeExist)))
    } else {
      Nil
    }
  }

}
