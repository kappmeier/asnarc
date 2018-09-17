package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.entity.{SpecialFoodWaitPeriod, TimedEntity}
import de.kappmeier.asnarc.transitions.{StateTransition, WorldTransition}

import scala.collection.mutable
import scala.concurrent.duration.{Duration, MILLISECONDS, SECONDS}
import scala.math.Ordering

class AsnarcGameImpl(level: String) extends AsnarcGame {
  override val player = new mutable.Queue[Body]()
  override val stepTime = 100

  var board = new AsnarcBoard(level)

  override var state = AsnarcState(new Player(board.cols / 2, board.rows / 2), dead = false)

  var frame = 0
  var turns = 0

  val initialFood: Point = board.freeLocation()
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
    val transitions: Seq[StateTransition] = updateMove(state.player)

    transitions.foreach {
      case w: WorldTransition => w.updateWorld(this)
    }

    // TODO transform this special objects into general entities
    while (timedTransitions.headOption.getOrElse(AsnarcGameImpl.FakeEntity).time.equals(time())) {
      timedTransitions.dequeue().update(this)
    }
  }

  def updateMove(element: ActionElement): Seq[StateTransition] = {
    val at: StaticElement = board.elementAt(element.p)
    at.update(this)
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
    player.exists(element => element.p == p)
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
