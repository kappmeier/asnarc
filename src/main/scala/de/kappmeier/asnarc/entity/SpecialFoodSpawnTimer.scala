package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.game.{AsnarcGame, TimeConst}
import de.kappmeier.asnarc.transitions.{ReplaceEntity, SpecialFoodSpawn, StateTransition}

class SpecialFoodSpawnTimer(val time: Int) extends TimedEntity {

  /**
    * When the spawn time is reached, the timer emits some state transitions.
    *
    * It creates a new entity (special food) that is placed somewhere and exchanges itself with a new spawn timer.
    *
    * @param game
    * @return
    */
  override def update(game: AsnarcGame): Seq[StateTransition] = {
    if (game.time() == time) {
      val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
      Seq(new SpecialFoodSpawn(), new ReplaceEntity(this, new SpecialFoodSpawnTimer(appearAt)))
    } else {
      Nil
    }
  }

}
