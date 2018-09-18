package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcGame

case class MoveHead(d: Direction) extends WorldTransition {
  def updateWorld(game: AsnarcGame) {
    val newPlayerPos: Point = (game.state.player.head.p + d.direction) % (60, 40)
    game.state = game.state.copy(player = new Player(newPlayerPos, d))
  }
}
