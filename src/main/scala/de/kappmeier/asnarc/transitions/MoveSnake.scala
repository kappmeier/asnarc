package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.AsnarcGame

case class MoveSnake() extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame) {
    if (game.player.nonEmpty) {
      game.board = game.board.removeElement(game.player.dequeue().p)
    }
  }
}
