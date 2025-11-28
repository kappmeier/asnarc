package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions._
import scala.collection.immutable.{Queue, Set}

class Player private(head: SnakeHead, val moveDirection: Direction, private val body: Queue[SnakeElement],
                     val justTeleported: Boolean = false) extends Entity {

    /**
      * Initializes a snake that only consists of the head.
      *
      * @param p             the head location
      * @param moveDirection the direction it is moving
      */
    def this(p: Point, moveDirection: Direction) = {
        this(new SnakeHead(p, moveDirection), moveDirection, Queue.empty, false)
    }

    /**
      * Returns the length of the snake.
      *
      * @return the length of the snake
      */
    def length(): Int = {
        body.size + 1
    }

    def elementAt(i: Int): Element = {
        if (i == 0) head
        else body(body.size - i)
    }

    def snakeHead(): SnakeHead = {
        head
    }

    /** Tests whether a predicate holds for at least one element of the snake.
      *
      * @param predicate the predicate used to test elements.
      * @return `true` if the given predicate `p` is satisfied by at least one [[SnakeElement]], `false` otherwise
      */
    def exists(predicate: SnakeElement => Boolean): Boolean = {
        body.exists(predicate) || predicate(head)
    }

    def turnTo(d: Direction): Player = {
        new Player(head, d, body, false) // Reset teleport flag on direction change
    }

    /**
      * Moves the head one step into the current direction.
      *
      * @return
      */
    def step(newPlayerPos: Point, full: Boolean = false): (Player, SnakeElement) = {
        // Create a new Head at the position
        val newHead: SnakeHead = new SnakeHead(newPlayerPos, moveDirection)

        // Enqueue the new body element
        val newBody: (SnakeElement, Queue[SnakeElement]) = enlargeBody().dequeue

        // Return a new player instance
        (new Player(newHead, moveDirection, newBody._2, false), if (newBody._2.isEmpty) head else newBody._1)
    }

    /**
      * Moves the head one step into the current direction.
      *
      * @return
      */
    def extend(newPlayerPos: Point, full: Boolean = false): Player = {
        // Create a new Head at the position

        val newHead: SnakeHead = if (full) new SnakeHead(newPlayerPos, moveDirection, Set[Direction](Left, Right, Up, Down))
        else new SnakeHead(newPlayerPos, moveDirection)

        // Enqueue the new body element
        val newBody: Queue[SnakeElement] = enlargeBody()

        // Return a new player instance
        new Player(newHead, moveDirection, newBody, false)
    }


    /**
      * Creates a new [[SnakeElement]] at the position of the [head] and extends the snake body with it. Called when the
      * snake is moving.
      *
      * @return the new instance of the body
      */
    private def enlargeBody(): Queue[SnakeElement] = {
        val connects: Set[Direction] = head.connects
        val connectsNew: Set[Direction] = connects + moveDirection

        body.enqueue(new SnakeElement(head.p, connectsNew))
    }


    /**
      * When the field is empty, the snake moves on. When there is a wall or an element of the snake itself, it
      * dies. When the field is a teleport field, the player uses it to teleport.
      *
      * Observe that the logic for all elements is handled in this class, i.e. the `Player` creates a teleport action
      * when the object is on a [[Teleport]] field. The `Player` also detects its upcoming [[Death]] in front of a
      * [[Wall]].
      *
      * @param gameWorld the read only game world
      * @return the list of state transitions that should be applied to the game world
      */
    override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
        val currentPos = gameWorld.player.snakeHead().p

        // Check if the player is currently on a teleport
        if (!justTeleported) {
            val underneathElement = gameWorld.board.staticMap.get(currentPos)
            underneathElement match {
                case Some(t: Teleport) => return Seq(TeleportPlayer(t))
                case _ => // Continue with normal movement
            }
        }

        val nextElement: Element = gameWorld.board.elementAt(Player.nextPos(gameWorld))

        nextElement match {
            case _: Wall => Seq(Death())
            case _: SnakeElement => Seq(Death())
            case _: Food => Seq(AppendSnake())
            case _: SpecialFood => Seq(AppendSnake(full = true))
            case _ => Seq(MovePlayer())
        }
    }


    override def toString = s"Player($head, $body)"
}

object Player {
    def nextPos(gameWorld: AsnarcWorld): Point = {
        (gameWorld.player.snakeHead().p + gameWorld.player.moveDirection.direction) % (60, 40)
    }

  /**
   * Creates a new Player instance that is marked as just teleported. This can be used to prevent immediate
   * re-teleportation.
   *
   * @param player the initial player
   * @return a copy of the player which is marked as just teleported
   */
  def withJustTeleported(player: Player): Player = {
    new Player(player.snakeHead(), player.moveDirection,
               Queue.empty ++ (0 until player.length() - 1).map(i => player.elementAt(i + 1).asInstanceOf[SnakeElement]),
               true)
  }
}
