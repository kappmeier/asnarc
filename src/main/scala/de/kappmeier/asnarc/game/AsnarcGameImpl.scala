package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.entity._
import de.kappmeier.asnarc.transitions.{StateTransition, WorldTransition}

import scala.collection.immutable.{HashSet, Set}
import scala.collection.mutable
import scala.concurrent.duration.{Duration, MILLISECONDS, SECONDS}

class AsnarcGameImpl(level: String) extends AsnarcGame {
  override val stepTime = 100

  /** Collection of all entities in the game. To be moved to state. */
  private val createdBoard = new AsnarcBoard(level)
  private val player = new Player(createdBoard.cols / 2, createdBoard.rows / 2, Set.empty[Direction])
  // Init the game with the special food
  private val initialFood: Food = Food(createdBoard.freeLocation())
  private val createdEntities: HashSet[Entity] = HashSet(initialFood,
    new SpecialFoodSpawnTimer((TimeConst.TimeBetweenSpecialFood.toMillis / stepTime).asInstanceOf[Int]), player)
  override var state = AsnarcWorld(createdBoard, player, createdEntities, dead = false)

  state = state.copy(board = state.board.addElement(initialFood),
    entities = state.entities + initialFood)

  /**
    *
    * From the trait to retrieve and update the game status.
    *
    */
  def direction(): Direction = d

  def time(): Int = frame

  /**
    *
    * External game status modification, keys, frame updates, etc.
    *
    */

  def updateMove(): Unit = {
    val transitions: Seq[StateTransition] = state.entities.toSeq.flatMap(e => updateMove(e))

    transitions.foreach {
      case w: WorldTransition => w.updateWorld(this)
    }

  }

  def updateMove(entity: Entity): Seq[StateTransition] = {
    entity.update(this)
  }

  def isIllegal(p: Point): Boolean = state.board.outOfBounds(p) || hitSelf(p)

  var d = Direction.Left
  val keys = new mutable.Queue[Direction]

  def nextFrame(): Unit = {
    frame += 1
    if (keys.nonEmpty) {
      d = keys.dequeue
      turns += 1
    }
  }

  def newDirection(d: Direction): Unit = keys.enqueue(d)

  /**
    *
    * Some methods.
    *
    */

  def hitSelf(p: Point): Boolean = {
    player.body.exists(element => element.p == p)
  }


}

object AsnarcGameImpl {
}

object TimeConst {
  val TimeBetweenSpecialFood = Duration(19, SECONDS)
  val TimeForSpecialFood = Duration(5500, MILLISECONDS)
}
