package de.kappmeier

import scala.collection.immutable.{HashMap, Set}
import Direction._

import scala.collection.mutable

case class LevelGenerator() {

    val drawColors: HashMap[Char, Set[Direction]] = collection.immutable.HashMap[Char, Set[Direction]](
        '─' -> Set(Direction.LEFT, Direction.RIGHT),
        '│' -> Set(Direction.DOWN, Direction.UP),
        '┌' -> Set(Direction.DOWN, Direction.RIGHT),
        '┐' -> Set(Direction.DOWN, Direction.LEFT),
        '┘' -> Set(Direction.LEFT, Direction.UP),
        '└' -> Set(Direction.RIGHT, Direction.UP),
        '├' -> Set(Direction.DOWN, Direction.RIGHT, UP),
        '┬' -> Set(Direction.DOWN, Direction.RIGHT),
        '┤' -> Set(Direction.DOWN, Direction.LEFT, UP),
        '┴' -> Set(Direction.LEFT, Direction.RIGHT, UP),
        '┼' -> Set(Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP),
        ' ' -> Set()
    )

    def breakBlocks(input: String): Array[String] = input.split(";")

    def breakBlock(input: String): (Point, Char) = {
        val parts = input.split(",")
        (Point(parts(0).toInt,parts(1).toInt),parts(2).toCharArray.apply(0))
    }

    def generate(levelInput: String): mutable.Queue[Element] = {
        val wall: mutable.Queue[Element] = mutable.Queue[Element]()
        val input: Array[(Point, Char)] = breakBlocks(levelInput).map(x => breakBlock(x))

        input.map(e => wall.enqueue(Wall(e._1, drawColors(e._2))))
        wall
    }
}

object LevelGenerator {

}