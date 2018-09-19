package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.entity.{Entity, Player}

/**
  * The immutable game world.
  *
  * @param player
  * @param dead
  */
case class AsnarcWorld(player: Player,
                       entities: List[Entity],
                       dead: Boolean
                      )
