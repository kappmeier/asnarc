package de.kappmeier.asnarc.entity

import de.kappmeier.asnarc.AsnarcSpec
import de.kappmeier.asnarc.board.Direction.{Direction, Down, Left, Right, Up}
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.elements.{SnakeElement, SnakeHead}

import scala.collection.immutable.Set

class PlayerTest extends AsnarcSpec {
    "initialization" should "create head only instance" in {
        val fixture: Player = new Player(Point(3, 4), Right)

        fixture.length() should be(1)
    }

    "movement" should "correctly move head" in {

        val fixture: Player = new Player(PlayerTest.StartPoint, Right)

        val fixtureHead = fixture.snakeHead()


        val (newPlayer, removedElement): (Player, SnakeElement) = fixture.step(PlayerTest.TargetPoint)

        newPlayer.length() should be(1)
        newPlayer.snakeHead().p should be(PlayerTest.TargetPoint)
        removedElement should be(fixtureHead)
        newPlayer.snakeHead().connects should be(Set[Direction](Left))
    }

    "extension" should "append a head and replace old with body element" in {
        val fixture: Player = new Player(PlayerTest.StartPoint, Right)

        val newPlayer: Player = fixture.extend(PlayerTest.TargetPoint)

        newPlayer.length() should be(2)
        newPlayer.elementAt(0) shouldBe a[SnakeHead]
        newPlayer.elementAt(1) shouldBe a[SnakeElement]
    }

    it should "correctly set up connections" in {
        val fixture: Player = new Player(PlayerTest.StartPoint, Right)

        val newPlayer: Player = fixture.extend(PlayerTest.TargetPoint)

        newPlayer.length() should be(2)
        newPlayer.snakeHead().p should be(PlayerTest.TargetPoint)

        // Check connections
        newPlayer.snakeHead().connects should be(Set[Direction](Left))
        newPlayer.elementAt(1).p should be(PlayerTest.StartPoint)
        newPlayer.elementAt(1).connects should be(Set[Direction](Right, Left))
    }

    "turning" should "connect correctly" in {
        val initPlayer: Player = new Player(PlayerTest.StartPoint, Left)
        val twoElements: Player = initPlayer.extend(leftOf(initPlayer.snakeHead().p))
        val threeElements: Player = twoElements.extend(leftOf(twoElements.snakeHead().p))

        threeElements.length() should be(3)
        threeElements.snakeHead().p should be(Point(1, 4))

        val fixture: Player = threeElements.turnTo(Direction.Up)

        val (check, _): (Player, SnakeElement) = fixture.step(aboveOf(threeElements.snakeHead().p))

        check.length() should be(3)
        check.elementAt(0).connects should be(Set[Direction](Down))
        check.elementAt(1).connects should be(Set[Direction](Up, Right))
        check.elementAt(2).connects should be(Set[Direction](Left, Right))
    }

    it should "connect turns correctly with full head" in {
        val initPlayer: Player = new Player(PlayerTest.StartPoint, Left)
        val twoElements: Player = initPlayer.extend(leftOf(initPlayer.snakeHead().p))
        val threeElements: Player = twoElements.extend(leftOf(twoElements.snakeHead().p), full = true)

        threeElements.length() should be(3)
        threeElements.snakeHead().p should be(Point(1, 4))
        threeElements.snakeHead().connects should be(Set[Direction](Left, Right, Up, Down))

        val fixture: Player = threeElements.turnTo(Up)

        val (check, removedElement): (Player, SnakeElement) = fixture.step(aboveOf(threeElements.snakeHead().p))

        check.length() should be(3)
        check.elementAt(0).connects should be(Set[Direction](Down))
        check.elementAt(1).connects should be(Set[Direction](Up, Right, Down, Left))
        check.elementAt(2).connects should be(Set[Direction](Left, Right))
    }

    private def leftOf(p: Point): Point = {
        Point(p.x - 1, p.y)
    }

    private def aboveOf(p: Point): Point = {
        Point(p.x, p.y - 1)
    }
}

object PlayerTest {
    val StartPoint: Point = Point(3, 4)
    val TargetPoint: Point = Point(7, 10)
}
