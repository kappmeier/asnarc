package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction
import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.entity._
import de.kappmeier.asnarc.transitions.{StateTransition, WorldTransition}

import scala.collection.immutable.HashSet
import scala.concurrent.duration.{Duration, MILLISECONDS, SECONDS}

/**
  * The game running class. Creates an initial game world and updates the game world based on current input.
  *
  * @param level
  */
class AsnarcGameImpl(level: String) extends AsnarcGame {
  override val stepTime = 100

  def initGameWorld(): AsnarcWorld = {
    /** Collection of all entities in the game. To be moved to state. */
    val createdBoard = new AsnarcBoard(level)
    val player = new Player(createdBoard.cols / 2, createdBoard.rows / 2, Direction.Left)
    // Init the game with the special food
    val initialFood: Food = Food(createdBoard.freeLocation())


    val timeExist = (TimeConst.TimeForSpecialFood.toMillis / stepTime).asInstanceOf[Int]
    val timeBetween = (TimeConst.TimeBetweenSpecialFood.toMillis / stepTime).asInstanceOf[Int]

    val createdEntities: HashSet[Entity] = HashSet(initialFood,
      new SpecialFoodSpawnTimer(timeBetween, timeBetween, timeExist), player)
    val state = AsnarcWorld(createdBoard, player, createdEntities, dead = false)

    state.copy(board = state.board.addElement(initialFood),
      entities = state.entities + initialFood)
  }

  /**
    *
    * External game status modification, keys, frame updates, etc.
    *
    */

  def updateMove(state: AsnarcWorld): AsnarcWorld = {
    val transitions: Seq[StateTransition] = state.entities.toSeq.flatMap(e => updateMove(state, e))

    var lastState: AsnarcWorld = state
    for (stateTransition: StateTransition <- transitions) {
      stateTransition match {
        case w: WorldTransition => lastState = w.updateWorld(lastState)
      }
    }
    //transitions.foreach {
    //  case w: WorldTransition => w.updateWorld(this)
    //}
    lastState
  }

  def updateMove(state: AsnarcWorld, entity: Entity): Seq[StateTransition] = {
    entity.update(state)
  }

  def nextFrame(gameWorld: AsnarcWorld): AsnarcWorld = {
    gameWorld.copy(time = gameWorld.time + 1)
  }

  def newDirection(gameWorld: AsnarcWorld, d: Direction): AsnarcWorld = {
    val oldPlayer = gameWorld.player
    val newPlayer = new Player(oldPlayer.head.p, d, oldPlayer.body)

    val entities = gameWorld.entities - oldPlayer + newPlayer

    gameWorld.copy(player = newPlayer, entities = entities)
  }


}

object AsnarcGameImpl {
}

object TimeConst {
  val TimeBetweenSpecialFood = Duration(19, SECONDS)
  val TimeForSpecialFood = Duration(5500, MILLISECONDS)
}
