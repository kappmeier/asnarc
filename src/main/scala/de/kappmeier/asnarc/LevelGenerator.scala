package de.kappmeier.asnarc

import de.kappmeier.asnarc.Direction._

import scala.collection.immutable.{HashMap, Set}
import scala.collection.mutable

case class LevelGenerator() {

    /**
      * Generates the [[Wall]] elements belonging to a level given as `String`.
      *
      * @param levelInput the input as string
      * @return the static initial definition of a level
      */
    def generateBoard(levelInput: String): mutable.Queue[Element] = {
        val input: Seq[(Point, Char)] = breakBlocks(levelInput).map(x => breakBlock(x))
        mutable.Queue[Element](input.map(element => LevelGenerator.wallFor(element)): _*)
    }

    /**
      * Breaks the level into parts for each row. The level description contains the information for each row separated
      * by `;`.
      *
      * @param input level description for multiple lines
      * @return
      */
    def breakBlocks(input: String): Seq[String] = input.split(";")

    def breakBlock(input: String): (Point, Char) = {
        val parts = input.split(",")
        (Point(parts(0).toInt,parts(1).toInt),parts(2).toCharArray.apply(0))
    }
}

object LevelGenerator {
    /**
      * Maps the level description `Char`s to the direction with which the corresponding [[Wall]] will be connected
      * to.
      */
    val BoundMap: HashMap[Char, Set[Direction]] = collection.immutable.HashMap[Char, Set[Direction]](
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

    /**
      * Returns a [[Wall]] element belonging to a given `Char` of the level description at the given location.
      *
      * @param entry the location and type of the [[Wall]]
      * @return the new [[Wall]] element
      */
    def wallFor(entry: (Point, Char)): Wall = Wall(entry._1, BoundMap(entry._2))
}