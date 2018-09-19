package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.entity.{Entity, Player}

import scala.collection.immutable.HashSet

/**
  * The immutable game world. Modified in each update step.
  *
  * @param player
  * @param dead
  */
case class AsnarcWorld(board: AsnarcBoard,
                       player: Player,
                       entities: HashSet[Entity],
                       dead: Boolean
                      )
