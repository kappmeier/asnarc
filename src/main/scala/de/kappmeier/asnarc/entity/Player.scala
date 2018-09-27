package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements._
import de.kappmeier.asnarc.game.AsnarcWorld
import de.kappmeier.asnarc.transitions._

import scala.collection.immutable.{Queue, Set}

class Player private(head: SnakeHead, val moveDirection: Direction, private val body: Queue[SnakeElement]) extends Entity {

    /**
      * Initializes a snake that only consists of the head.
      *
      * @param p             the head location
      * @param moveDirection the direction it is moving
      */
    def this(p: Point, moveDirection: Direction) = {
        this(new SnakeHead(p, moveDirection), moveDirection, Queue.empty)
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
        val newPlayer = new Player(head, d, body)
        newPlayer
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
        (new Player(newHead, moveDirection, newBody._2), if (newBody._2.isEmpty) head else newBody._1)
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
        new Player(newHead, moveDirection, newBody)
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
      * dies.
      *
      * @param gameWorld the read only game world
      * @return the list of state transitions that should be applied to the game world
      */
    override def update(gameWorld: AsnarcWorld): Seq[StateTransition] = {
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
}
