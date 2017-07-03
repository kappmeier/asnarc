package de.kappmeier

import scala.collection.immutable.{HashMap, Set}
import Direction._

import scala.collection.mutable

case class LevelGenerator() {

    val drawColors: HashMap[Char, Set[Direction]] = collection.immutable.HashMap[Char, Set[Direction]](
        '─' -> Set(Direction.Left, Direction.Right),
        '│' -> Set(Direction.Down, Direction.Up),
        '┌' -> Set(Direction.Down, Direction.Right),
        '┐' -> Set(Direction.Down, Direction.Left),
        '┘' -> Set(Direction.Left, Direction.Up),
        '└' -> Set(Direction.Right, Direction.Up),
        '├' -> Set(Direction.Down, Direction.Right, Up),
        '┬' -> Set(Direction.Down, Direction.Right),
        '┤' -> Set(Direction.Down, Direction.Left, Up),
        '┴' -> Set(Direction.Left, Direction.Right, Up),
        '┼' -> Set(Direction.Down, Direction.Left, Direction.Right, Direction.Up),
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