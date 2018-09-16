package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.LevelGenerator
import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.entity.{SpecialFoodWaitPeriod, TimedEntity}
import de.kappmeier.asnarc.transitions.{StateTransition, WorldTransition}

import scala.collection.mutable
import scala.concurrent.duration.{Duration, MILLISECONDS, SECONDS}
import scala.math.Ordering
import scala.util.Random

class SnakeGameImpl(level: String) extends SnakeGame {
  override val snake = new mutable.Queue[Body]()
  override val stepTime = 100

  val rows = 40
  val cols = 60

  override var state = SnakeGameState(new Player(cols / 2, rows / 2), dead = false)

  val map = new mutable.HashMap[Point, StaticElement]()

  var frame = 0
  var turns = 0

  val doorSize: Int = 3

  var lg = new LevelGenerator()

  val wall: mutable.Queue[Element] = lg.generateBoard64(level)
  initLevel()

  val initialFood: Point = freeLocation()
  addElement(initialFood, Food(initialFood))

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

  def removeElement(p: Point): Unit = {
    map -= p
  }

  def addElement(p: Point, element: StaticElement): Unit = {
    map(p) = element
  }

  def freeLocation(): Point = {
    val x: Int = Random.nextInt(cols)
    val y: Int = Random.nextInt(rows)
    val newPoint = Point(x, y)
    if (map.get(newPoint).isEmpty) newPoint else freeLocation()
  }

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
    while (timedTransitions.headOption.getOrElse(SnakeGameImpl.FakeEntity).time.equals(time())) {
      timedTransitions.dequeue().update(this)
    }
  }

  def updateMove(element: ActionElement): Seq[StateTransition] = {
    val at: StaticElement = elementAt(element.p)
    at.update(this)
  }

  def elementAt(p: Point): StaticElement = map.getOrElse(p, Empty)

  def isIllegal(p: Point): Boolean = outOfBounds(p) || hitSelf(p)

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
  def outOfBounds(p: Point): Boolean = {
    wall.exists(element => element.p == p)
  }

  def hitSelf(p: Point): Boolean = {
    snake.exists(element => element.p == p)
  }

  /**
    *
    * Initialization of level.
    *
    */
  def initLevel(): Unit = {
    wall.foreach(element => addElement(element.p, element.asInstanceOf[Wall]))
  }

}

object SnakeGameImpl {
  /**
    * A null object for empty transition lists.
    */
  private val FakeEntity: TimedEntity = new TimedEntity {
    override def update(game: SnakeGame) = Nil

    override val time: Int = Int.MaxValue
  }

}

object TimeConst {
  val TimeBetweenSpecialFood = Duration(19, SECONDS)
  val TimeForSpecialFood = Duration(5500, MILLISECONDS)
}
