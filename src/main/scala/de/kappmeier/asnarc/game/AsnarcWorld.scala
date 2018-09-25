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
    player.body.exists(element => element.p == p)
  }

  def isIllegal(p: Point): Boolean = board.outOfBounds(p) || hitSelf(p)


  /**
    *
    * From the trait to retrieve and update the game status.
    *
    */
  def direction(): Direction = player.head.moveDirection
}

object AsnarcWorld {
  def apply(board: AsnarcBoard,
            player: Player,
            entities: HashSet[Entity],
            dead: Boolean): AsnarcWorld = {
    AsnarcWorld(0, board, player, entities, dead)
  }
}