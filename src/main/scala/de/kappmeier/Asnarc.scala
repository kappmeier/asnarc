package de.kappmeier
import scala.util.Random
import scala.math.Ordering
import scala.collection.immutable.Set
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.duration.MILLISECONDS
import scala.concurrent.duration.SECONDS

case class Point(x: Int, y: Int){
  def +(p: Point) = Point(x + p.x, y + p.y)
  def /(d: Int) = Point(x / d, y / d)
  def %(w: Int, h: Int) = Point((x + w) % w, (y + h) % h)
}

object Direction extends Enumeration {
    type Direction = Value
    val LEFT = DirectionVal(Point(-1, 0), 37)
    val RIGHT = DirectionVal(Point(1, 0), 39)
    val UP = DirectionVal(Point(0, -1), 38)
    val DOWN = DirectionVal(Point(0, 1), 40)
    case class DirectionVal(direction: Point, keyCode: Int) extends super.Val()
    implicit def convert(value: Value): DirectionVal = value.asInstanceOf[DirectionVal]
    val oppositeMap: scala.collection.immutable.Map[DirectionVal, DirectionVal] = Map(LEFT -> RIGHT, RIGHT -> LEFT, UP -> DOWN, DOWN -> UP)
    def byKeyCode(keyCode: Int): Option[DirectionVal] = {
        if (keyCode == LEFT.keyCode) {
            return Some(LEFT)
        } else if (keyCode == RIGHT.keyCode) {
            return Some(RIGHT)
        } else if (keyCode == UP.keyCode) {
            return Some(UP)
        } else if (keyCode == DOWN.keyCode) {
            return Some(DOWN)
        }
        None
    }
    implicit def opposite(d: DirectionVal): DirectionVal = oppositeMap.get(d).get
}
import Direction._

trait Entity

trait Element {
    def p: Point
    def connects: Set[Direction]
}

/**
 * Passive elements.
 */
trait StaticElement extends Entity with Element {
    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition])
}

case class Wall(p: Point, connects: Set[Direction]) extends Entity with StaticElement{
    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq(Death()))
    }
}

case class Food(p: Point) extends Entity with StaticElement {
    override def connects = Set.empty[Direction]
    
    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq(MoveHead(game.direction()), EatFood(this), AppendSnake(game.player)))
    }
}

case class SpecialFood(p: Point) extends Entity with StaticElement {
    override def connects = Set.empty[Direction]

    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq(MoveHead(game.direction()), EatSpecialFood(this), AppendSnake(game.player, full = true)
        ))
    }
}

case class Body(p: Point, connects: Set[Direction]) extends Entity with StaticElement {
    def this(e: Player, connectsAlso: Direction) {
        this(e.p, e.connects + connectsAlso)
    }

    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq(Death()))
    }
}

object Empty extends StaticElement with Entity {
    override def p = Point(-1, -1)
    override def connects = Set.empty[Direction]

    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq(MoveHead(game.direction()), AppendSnake(game.player), MoveSnake()))
    }
}

/**
 * Elements that are updated in each iteration.
 */
trait ActionElement extends Entity with Element

case class Player(p: Point, connects: Set[Direction]) extends Entity with ActionElement {
    def this(x: Int, y: Int) {
        this(Point(x,y), Set.empty[Direction])
    }

    def this(p: Point, moveDirection: Direction) {
        this(p, Set[Direction](opposite(moveDirection)))
    }

    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        (Seq.empty, Seq.empty)
    }
}

trait TimedEntity {
    val time: Int
    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition])
}

trait SnakeEvent

object Nothing extends SnakeEvent {

}

trait ToEvent extends SnakeEvent {
    def applyTo(entity:Entity) : (Entity, Seq[SnakeEvent])
}

trait StateTransition

// Events that spawn new Entitys
trait WorldTransition extends StateTransition {
    def updateWorld(game: SnakeGame)
}

case class MoveHead(d: Direction) extends WorldTransition {
    def updateWorld(game: SnakeGame) {
        val newPlayerPos = (game.player.p + d.direction) % (60, 40)
        game.player = new Player(newPlayerPos, d)
    }
}

case class Death() extends StateTransition with WorldTransition {
    def updateWorld(game: SnakeGame) {
        game.dead = true
    }
}

case class EatFood(f: Food) extends StateTransition with WorldTransition {
    def updateWorld(game: SnakeGame) {
        game.removeElement(f.p)
        val foodPosition = game.freeLocation()
        game.addElement(foodPosition, Food(foodPosition))
    }
}

case class EatSpecialFood(f: SpecialFood) extends StateTransition with WorldTransition {
    def updateWorld(game: SnakeGame) {
        // Remove the food
        game.removeElement(f.p)
        
        // Place a new timer
        val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
        game.addTimer(SpecialFoodWaitPeriod(appearAt))
    }
}

case class AppendSnake(player: Player, full: Boolean) extends StateTransition with WorldTransition {
    def this(player: Player) = this(player, false)

    def updateWorld(game: SnakeGame) {
    
        val newElement: Body = if (full) Body(player.p, Set[Direction](LEFT, RIGHT, UP, DOWN)) else new Body(player, game.direction())
        game.snake.enqueue(newElement)
        game.addElement(player.p, newElement)
    }
}

// Companion object
object AppendSnake {
  def apply(player: Player) = new AppendSnake(player)
}

case class MoveSnake() extends StateTransition with WorldTransition {
    def updateWorld(game: SnakeGame) {
        if (game.snake.nonEmpty) {
            game.removeElement(game.snake.dequeue().p)
        }
    }
}

/**
 * Collection of state for the snake game that is to be modified by [[Entity]].
 */
trait SnakeGame {
    // Step time in milliseconds
    val stepTime: Int 
    var snake: mutable.Queue[Body]
    var dead: Boolean
    var player: Player
    def time(): Int
    def freeLocation(): Point
    def removeElement(p: Point): Unit
    def addElement(p: Point, element: StaticElement): Unit
    def direction(): Direction
    def addTimer(te: TimedEntity): Unit
    def elementAt(p: Point): StaticElement
}

case class SpecialFoodWaitPeriod(time: Int) extends TimedEntity {
    /**
     * Creates a new timer to create food.
     */
    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        val foodPosition = game.freeLocation()
        val specialFood = SpecialFood(foodPosition)

        val disappearAt = game.time() + (TimeConst.TimeForSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
        
        // timer to disappear
        val timer = SpecialFoodDisappearPeriod(specialFood, disappearAt)
    
        game.addElement(foodPosition, specialFood)
        
        game.addTimer(timer)
        (Seq.empty, Seq.empty)
    }
}

object TimeConst {
    val TimeBetweenSpecialFood = Duration(19, SECONDS)
    val TimeForSpecialFood = Duration(5500, MILLISECONDS)
}

/**
 * Timer until food disappears
 */
case class SpecialFoodDisappearPeriod(food: SpecialFood, time: Int) extends TimedEntity {

    def update(game: SnakeGame): (Seq[SnakeEvent], Seq[StateTransition]) = {
        val potentialFood = game.elementAt(food.p)
        if (potentialFood == food) {
            val appearAt = game.time() + (TimeConst.TimeBetweenSpecialFood.toMillis / game.stepTime).asInstanceOf[Int]
            game.removeElement(food.p)
            game.addTimer(SpecialFoodWaitPeriod(appearAt))
        }
        (Seq.empty, Seq.empty)
    }
}

class SnakeGameImpl(level: String) extends SnakeGame {
    override var snake = new mutable.Queue[Body]()
    override var dead = false
    override val stepTime = 100

    val rows = 40
    val cols = 60

    override var player = new Player(cols / 2, rows / 2)

    val map = new mutable.HashMap[Point, StaticElement]()
    
    var frame = 0
    var turns = 0


    val initialFood: Point = freeLocation()
    val doorSize: Int = 3

    var lg = new LevelGenerator()

    val wall: mutable.Queue[Element] = lg.generate(level)
    initLevel()

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

    def updateMove():Unit = {
        val transitions: Seq[StateTransition] = updateMove(player)
        
        transitions.foreach {
            case w: WorldTransition => w.updateWorld(this)
        }

        // TODO transform this special objects into general entities
        while (timedTransitions.nonEmpty && timedTransitions.head.time.equals(time())) {
            timedTransitions.dequeue().update(this)
        }
    }
    
    def updateMove(element: ActionElement): Seq[StateTransition] = {
        val at: StaticElement = elementAt(element.p)
        val (_, transitions) = at.update(this)
        transitions
    }

    def elementAt(p: Point): StaticElement = map.getOrElse(p, Empty)

    def newFood(): Point = {
        val x: Int = Random.nextInt(cols)
        val y: Int = Random.nextInt(rows)
        val newPoint = Point(x, y)
        if (isIllegal(newPoint)) newFood() else newPoint
    }
    
    def isIllegal(p: Point): Boolean = outOfBounds(p) || hitSelf(p)

    var d = Direction.LEFT
    val keys = new mutable.Queue[DirectionVal]

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
        wall.foreach( element => addElement(element.p, element.asInstanceOf[Wall]))
    }

    def initLevelFull(): Unit = {
        for (i <- 1 until cols - 1) {
            addWallElement(Point(i, 0), Set(Direction.LEFT, Direction.RIGHT))
            addWallElement(Point(i, rows - 1), Set(Direction.LEFT, Direction.RIGHT))
        }
        for (i <- 1 until rows - 1) {
            addWallElement(Point(0, i), Set(Direction.UP, Direction.DOWN))
            addWallElement(Point(cols - 1, i), Set(Direction.UP, Direction.DOWN))
        }
        addWallElement(Point(0, 0), Set(Direction.RIGHT, Direction.DOWN))
        addWallElement(Point(cols - 1, 0), Set(Direction.LEFT, Direction.DOWN))
        addWallElement(Point(0, rows - 1), Set(Direction.RIGHT, Direction.UP))
        addWallElement(Point(cols - 1, rows - 1), Set(Direction.LEFT, Direction.UP))
    }
    
    def addWallElement(p: Point, c: Set[Direction]): Unit = {
        val wallElement = Wall(p, c)
        wall.enqueue(wallElement)
        addElement(p, wallElement)
    }
    
}
