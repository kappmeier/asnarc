package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.{AsnarcGame, AsnarcWorld}

case class MoveHead(d: Direction) extends WorldTransition {
  override def updateWorld(game: AsnarcGame): AsnarcWorld = {
    val newPlayerPos: Point = Player.nextPos(game)
    val newPlayer = new Player(newPlayerPos, d, game.state.player.body)
    game.state = game.state.copy(player = newPlayer,
      entities = game.state.entities.-(game.state.player).+(newPlayer),
      board = game.state.board.addElement(newPlayer.head).removeElement(game.state.player.head.p)
    )
    game.state
  }
}

