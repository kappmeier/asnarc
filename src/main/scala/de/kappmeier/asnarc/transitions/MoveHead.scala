package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

case class MoveHead(d: Direction) extends WorldTransition {
  override def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    val newPlayerPos: Point = Player.nextPos(gameWorld)

    val newPlayer = new Player(newPlayerPos, d, gameWorld.player.body)
    gameWorld.copy(player = newPlayer,
      entities = gameWorld.entities.-(gameWorld.player).+(newPlayer),
      board = gameWorld.board.addElement(newPlayer.head).removeElement(gameWorld.player.head.p)
    )
  }
}

