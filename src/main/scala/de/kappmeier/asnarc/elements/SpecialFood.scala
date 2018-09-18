package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.Set

case class SpecialFood(p: Point) extends SingleElementEntity {
  override def connects = Set.empty[Direction]

  /**
    * The food updates the snake when it is at the same position as the snake head.
    *
    * @param game the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(game: AsnarcGame): Seq[StateTransition] = {
    if (game.state.player.head.p == p) {
      Seq(EatSpecialFood(this), AppendSnake(game.state.player, full = true))
    } else Nil
  }
}
