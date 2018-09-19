package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class MoveSnake() extends StateTransition with WorldTransition {
  def updateWorld(game: AsnarcGame): AsnarcWorld = {
    if (game.player.body.nonEmpty) {
      game.board = game.board.removeElement(game.player.body.dequeue().p)
    }
    game.state
  }
}
