package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.{Entity, Player}

import scala.collection.immutable.HashSet

/**
  * The immutable game world. Modified in each update step.
  *
  * @param player
  * @param dead
  */
case class AsnarcWorld(time: Int,
                       board: AsnarcBoard,
                       player: Player,
                       entities: HashSet[Entity],
                       dead: Boolean
                      ) {

  /**
    *
    * Some methods.
    *
    */

  def hitSelf(p: Point): Boolean = {
    player.exists(element => element.p == p)
  }

  def isIllegal(p: Point): Boolean = board.outOfBounds(p) || hitSelf(p)
}

object AsnarcWorld {
  def apply(board: AsnarcBoard,
            player: Player,
            entities: HashSet[Entity],
            dead: Boolean): AsnarcWorld = {
    AsnarcWorld(0, board, player, entities, dead)
  }
}
