package de.kappmeier.asnarc.board

import de.kappmeier.asnarc.LevelGenerator
import de.kappmeier.asnarc.elements.{Element, Empty, Wall}

import scala.collection.mutable
import scala.util.Random

/**
 * Represents the rectangular board area of the Asnarc game. Inside its width and height, the board contains square
 * elements of different types. Each call to update the board will return a new instance.
 *
 * @param map set of elements present on the board
 */
class AsnarcBoard(val map: scala.collection.immutable.Map[Point, Element]) {

  /**
   * Initializes an empty game board with a level as string. The initialization will contain only the static parts of
   * the game board, i.e. no food and no player location.
   *
   * @param level base64 encoded level
   */
  def this(level: String) {
    this(AsnarcBoard.getFromLevel(level))
  }

  /**
   * The height of the game board.
   */
  val rows: Integer = map.map(_._1.y).max - map.map(_._1.y).min + 1
  /**
   * The width of the game board.
   */
  val cols: Integer = map.map(_._1.x).max - map.map(_._1.x).min + 1

  // Board data
  def addElement(element: Element): AsnarcBoard = {
    new AsnarcBoard(map + (element.p -> element))
  }

  def removeElement(element: Element): AsnarcBoard = {
    new AsnarcBoard(map - element.p)
  }

  def elementAt(p: Point): Element = map.getOrElse(p, Empty)

  def outOfBounds(p: Point): Boolean = {
    map.contains(p) && map(p).isInstanceOf[Wall]
  }

  /**
   * Calculates a random location on the board that is free.
   *
   * @return a free location on the board
   */
  def freeLocation(): Point = {
    val x: Int = Random.nextInt(cols)
    val y: Int = Random.nextInt(rows)
    val newPoint = Point(x, y)
    if (!map.contains(newPoint)) newPoint else freeLocation()
  }

}

object AsnarcBoard {
  private def getFromLevel(level: String): scala.collection.immutable.Map[Point, Element] = {
    val mutableMap: scala.collection.mutable.HashMap[Point, Element] = new mutable.HashMap[Point, Element]()

    // Init
    val levelGenerator = new LevelGenerator()

    val elements: mutable.Queue[Element] = levelGenerator.generateBoard64(level)
    elements.foreach(element => mutableMap += (element.p -> element))

    mutableMap.toMap
  }

}
