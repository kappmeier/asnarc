package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions.{AppendSnake, EatSpecialFood, MoveHead, StateTransition}

import scala.collection.immutable.Set

case class SpecialFood(p: Point) extends Entity with StaticElement {
  override def connects = Set.empty[Direction]

  def update(game: AsnarcGame): Seq[StateTransition] =
    Seq(MoveHead(game.direction()), EatSpecialFood(this), AppendSnake(game.state.player, full = true))
}
