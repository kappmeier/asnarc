package de.kappmeier.asnarc.transitions

import org.scalajs.dom.console

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{SnakeElement, Teleport}
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

case class TeleportPlayer() extends StateTransition with WorldTransition {
  def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
    val startTeleportPos: Point = Player.nextPos(gameWorld)
    console.log("Teleport at: " + startTeleportPos)

    // Search for the other Teleport
    val filteredElements = gameWorld.board.staticMap.collect { case (key, value: Teleport) => (key, value) }

    console.log("Potential Teleports: " + filteredElements)

    val teleportOption = filteredElements.collectFirst { case (key, value: Teleport) if !key.equals(startTeleportPos) => key }
    val newPlayerPosition = teleportOption.get
    console.log("Teleporting to " + teleportOption)

    val (newPlayer, removedElement): (Player, SnakeElement) = gameWorld.player.step(newPlayerPosition)
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
