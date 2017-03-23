package de.kappmeier

import org.scalajs.dom

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
        dom.console.log("breakBlock: '" + input)
        val parts = input.split(",")
        (Point(parts(0).toInt,parts(1).toInt),parts(2).toCharArray.apply(0))
    }

    def generate(levelInput: String): mutable.Queue[Element] = {
        val wall: mutable.Queue[Element] = mutable.Queue[Element]()

        val unused = "0,0,┌;1,0,─;2,0,─;3,0,─;4,0,─;5,0,─;6,0,─;7,0,─;8,0,─;9,0,─;10,0,─;11,0,─;12,0,─;13,0,─;14,0,─;" +
                "15,0,─;16,0,─;17,0,─;18,0,─;19,0,─;20,0,─;21,0,─;22,0,─;23,0,─;24,0,─;25,0,─;26,0,─;33,0,─;34,0,─;35,0,─;" +
                "36,0,─;37,0,─;38,0,─;39,0,─;40,0,─;41,0,─;42,0,─;43,0,─;44,0,─;45,0,─;46,0,─;47,0,─;48,0,─;49,0,─;50,0,─;" +
                "51,0,─;52,0,─;53,0,─;54,0,─;55,0,─;56,0,─;57,0,─;58,0,─;59,0,┐;0,39,└;1,39,─;2,39,─;3,39,─;4,39,─;5,39,─;" +
                "6,39,─;7,39,─;8,39,─;9,39,─;10,39,─;11,39,─;12,39,─;13,39,─;14,39,─;15,39,─;16,39,─;17,39,─;18,39,─;19,39,─;" +
                "20,39,─;21,39,─;22,39,─;23,39,─;24,39,─;25,39,─;26,39,─;33,39,─;34,39,─;35,39,─;36,39,─;37,39,─;38,39,─;" +
                "39,39,─;40,39,─;41,39,─;42,39,─;43,39,─;44,39,─;45,39,─;46,39,─;47,39,─;48,39,─;49,39,─;50,39,─;51,39,─;" +
                "52,39,─;53,39,─;54,39,─;55,39,─;56,39,─;57,39,─;58,39,─;59,39,┘;0,1,│;0,2,│;0,3,│;0,4,│;0,5,│;0,6,│;0,7,│;" +
                "0,8,│;0,9,│;0,10,│;0,11,│;0,12,│;0,13,│;0,14,│;0,15,│;0,16,│;0,23,│;0,24,│;0,25,│;0,26,│;0,27,│;0,28,│;0,29,│;" +
                "0,30,│;0,31,│;0,32,│;0,33,│;0,34,│;0,35,│;0,36,│;0,37,│;0,38,│;59,1,│;59,2,│;59,3,│;59,4,│;59,5,│;59,6,│;" +
                "59,7,│;59,8,│;59,9,│;59,10,│;59,11,│;59,12,│;59,13,│;59,14,│;59,15,│;59,16,│;59,23,│;59,24,│;59,25,│;59,26,│;" +
                "59,27,│;59,28,│;59,29,│;59,30,│;59,31,│;59,32,│;59,33,│;59,34,│;59,35,│;59,36,│;59,37,│;59,38,│"

        val splitted = breakBlocks(levelInput)
        val input: Array[(Point, Char)] = splitted.map(x => breakBlock(x))

        input.map(e => wall.enqueue(Wall(e._1, drawColors(e._2))))
        wall
    }
}

object LevelGenerator {

}