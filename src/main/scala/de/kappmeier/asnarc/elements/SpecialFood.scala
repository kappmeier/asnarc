package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.Set

case class SpecialFood(p: Point, removeAt: Int) extends SingleElementEntity {
  override def connects = Set.empty[Direction]

  /**
    * The food updates the snake when it is at the same position as the snake head. When the game time is behind the
    * existence of the food, it emits transitions to destroys itself.
    *
    * @param gameWorld the read only game world
    * @return the list of state transitions that should be applied to the game world
    */
  override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
    if (gameWorld.player.snakeHead().p == p) {
      Seq(EatSpecialFood(this))
    } else if (gameWorld.time == removeAt) {
      Seq(RemoveEntity(this, Seq(this)))
    } else Nil
  }
}
