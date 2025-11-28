package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{SnakeElement, Teleport}
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

case class TeleportPlayer(startTeleport: Teleport) extends StateTransition with WorldTransition {
  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    val startTeleportPos: Point = gameWorld.player.snakeHead().p

    // Search for the other Teleport
    val filteredElements = gameWorld.board.staticMap.collect { case (key, value: Teleport) => (key, value) }

    val teleportOption = filteredElements.collectFirst { case (key, value: Teleport) if !key.equals(startTeleportPos) => key }
    val newPlayerPosition = teleportOption.get

    // Create a new player and mark it as just teleported
    val (tempNewPlayer, removedElement): (Player, SnakeElement) = gameWorld.player.step(newPlayerPosition)
    val newPlayer = Player.withJustTeleported(tempNewPlayer)

    val tempBoard = gameWorld.board
      .removeDynamicElement(removedElement)
      .removeDynamicElement(gameWorld.player.snakeHead())
      .addDynamicElement(newPlayer.snakeHead())

    gameWorld.copy(
      board = if (newPlayer.length() == 1) {
        tempBoard
      } else {
        tempBoard.addDynamicElement(newPlayer.elementAt(1))
      },
      entities = gameWorld.entities.-(gameWorld.player).+(newPlayer),
      player = newPlayer
    )
  }
}
