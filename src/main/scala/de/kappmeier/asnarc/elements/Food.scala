package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions.{EatFood, StateTransition}

import scala.collection.immutable.Set

/**
  * Default food.
  *
  * @param p
  */
case class Food(p: Point) extends SingleElementEntity {
  override def connects = Set.empty[Direction]

  override def update(game: AsnarcGame): Seq[StateTransition] = {
    if (game.state.player.head.p == p) {
      Seq(EatFood(this))
    } else Nil
  }
}
