package de.kappmeier.asnarc.transitions

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.SnakeElement
import de.kappmeier.asnarc.entity.Player
import de.kappmeier.asnarc.game.AsnarcWorld

case class MovePlayer() extends StateTransition with WorldTransition {
    def updateWorld(gameWorld: AsnarcWorld): AsnarcWorld = {
        val newPlayerPos: Point = Player.nextPos(gameWorld)

        val (newPlayer, removedElement): (Player, SnakeElement) = gameWorld.player.step(newPlayerPos)
        val tempBoard = gameWorld.board
                .removeDynamicElement(removedElement)
                .removeDynamicElement(gameWorld.player.snakeHead())
                .addDynamicElement(newPlayer.snakeHead())

        gameWorld.copy(
          board = if (newPlayer.length() == 1) tempBoard else tempBoard.addDynamicElement(newPlayer.elementAt(1)),
          entities = gameWorld.entities.-(gameWorld.player).+(newPlayer),
          player = newPlayer
          )
    }
}
