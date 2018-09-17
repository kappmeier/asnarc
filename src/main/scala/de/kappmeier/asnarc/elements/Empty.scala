package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions.{AppendSnake, MoveHead, MoveSnake, StateTransition}

import scala.collection.immutable.Set

object Empty extends StaticElement with Entity {
  override def p = Point(-1, -1)

  override def connects = Set.empty[Direction]

  def update(game: AsnarcGame): Seq[StateTransition] =
    Seq(MoveHead(game.direction()), AppendSnake(game.state.player), MoveSnake())
}
