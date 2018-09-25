package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions.{EatFood, StateTransition}

import scala.collection.immutable.Set

/**
  * Default food.
  *
  * @param p
  */
case class Food(p: Point) extends SingleElementEntity {
  override def connects = Set.empty[Direction]

  override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
    if (gameWorld.player.head.p == p) {
      Seq(EatFood(this))
    } else Nil
  }
}
