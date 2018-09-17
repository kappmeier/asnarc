package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.elements.Player
import de.kappmeier.asnarc.game.AsnarcGame

case class MoveHead(d: Direction) extends WorldTransition {
  def updateWorld(game: AsnarcGame) {
    val newPlayerPos = (game.state.player.p + d.direction) % (60, 40)
    game.state = game.state.copy(player = new Player(newPlayerPos, d))
  }
}
