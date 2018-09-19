package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class MoveHead(d: Direction) extends WorldTransition {
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    val newPlayerPos: Point = (game.state.player.head.p + d.direction) % (60, 40)
    val newPlayer = new Player(newPlayerPos, d)
    game.entities = game.entities.-(game.state.player).+(newPlayer)
    game.state = game.state.copy(player = newPlayer)
    game.state
  }
}
