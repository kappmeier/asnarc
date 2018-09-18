package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.entity._
import de.kappmeier.asnarc.transitions.{StateTransition, WorldTransition}

import scala.collection.immutable.Set
import scala.collection.mutable
import scala.concurrent.duration.{Duration, MILLISECONDS, SECONDS}
import scala.math.Ordering

class AsnarcGameImpl(level: String) extends AsnarcGame {
  var board = new AsnarcBoard(level)
  override val player = new Player(board.cols / 2, board.rows / 2, Set.empty[Direction])
  override val stepTime = 100

  override var state = AsnarcState(player, dead = false)

  var frame = 0
  var turns = 0

  override var initialFood: Point = board.freeLocation()
  board = board.addElement(initialFood, Food(initialFood))

  var specialFood: Option[Point] = None

  def timedElementOrder(te: TimedEntity): Int = -te.time

  val timedTransitions: mutable.PriorityQueue[TimedEntity] = mutable.PriorityQueue[TimedEntity]()(Ordering.by(timedElementOrder))

  addTimer(SpecialFoodWaitPeriod((TimeConst.TimeBetweenSpecialFood.toMillis / stepTime).asInstanceOf[Int]))

  /**
    *
    * From the trait to retrieve and update the game status.
    *
    */
  def direction(): Direction = d

  def addTimer(te: TimedEntity): Unit = {
    timedTransitions += te
  }

  def time(): Int = frame

  /**
    *
    * External game status modification, keys, frame updates, etc.
    *
    */

  def updateMove(): Unit = {
    val food: Food = board.elementAt(initialFood).asInstanceOf[Food]
    val specialFood: Entity = if (this.specialFood.isDefined) board.elementAt(this.specialFood.get).asInstanceOf[Entity] else Nothing

    val transitions: Seq[StateTransition] = updateMove(food) ++ updateMove(specialFood) ++ updateMove(state.player)

    transitions.foreach {
      case w: WorldTransition => w.updateWorld(this)
    }

    // TODO transform this special objects into general entities
    while (timedTransitions.headOption.getOrElse(AsnarcGameImpl.FakeEntity).time.equals(time())) {
      timedTransitions.dequeue().update(this)
    }
  }

  def updateMove(entity: Entity): Seq[StateTransition] = {
    entity.update(this)
  }

  def isIllegal(p: Point): Boolean = board.outOfBounds(p) || hitSelf(p)

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
  /**
    * A null object for empty transition lists.
    */
  private val FakeEntity: TimedEntity = new TimedEntity {
    override def update(game: AsnarcGame) = Nil

    override val time: Int = Int.MaxValue
  }

}

object TimeConst {
  val TimeBetweenSpecialFood = Duration(19, SECONDS)
  val TimeForSpecialFood = Duration(5500, MILLISECONDS)
}
