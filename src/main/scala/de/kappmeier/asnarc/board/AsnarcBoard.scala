package de.kappmeier.asnarc.board

import de.kappmeier.asnarc.LevelGenerator
import de.kappmeier.asnarc.elements.{Element, Empty, Wall}

import scala.annotation.tailrec
import scala.collection.mutable
import scala.util.Random

/**
 * Represents the rectangular board area of the Asnarc game. Inside its width and height, the board contains square
 * elements of different types. Each call to update the board will return a new instance.
 *
 * Some elements in the `staticMap` (e.g. [[Wall]]s) will create a collision with the player, while other entities can
 * be moved over/on.
 *
 * @param staticMap set of elements present on the board in the level
 * @param dynamicMap the set of elements dynamically placed on the board
 */
class AsnarcBoard(val staticMap: scala.collection.immutable.Map[Point, Element],
                  val dynamicMap: scala.collection.immutable.Map[Point, Element]) {

  /**
   * Initializes an empty game board with a level as string. The initialization will contain only the static parts of
   * the game board, i.e. no food and no player location.
   *
   * @param level base64 encoded level
   */
  def this(level: String) {
    this(AsnarcBoard.getFromLevel(level), Map.empty)
  }

  /**
   * The height of the game board.
   */
  val rows: Integer = staticMap.map(_._1.y).max - staticMap.map(_._1.y).min + 1
  /**
   * The width of the game board.
   */
  val cols: Integer = staticMap.map(_._1.x).max - staticMap.map(_._1.x).min + 1

  /**
   * AAdds a dynamic element to the board.
   *
   * @param element the element to be added
   * @return a board instance with the new state
   */
  def addDynamicElement(element: Element): AsnarcBoard = {
    new AsnarcBoard(staticMap, dynamicMap + (element.p -> element))
  }

  /**
   * Removes a dynamic element from the board.
   *
   * @param element the element to be removed
   * @return a board instance with the new state
   */
  def removeDynamicElement(element: Element): AsnarcBoard = {
    new AsnarcBoard(staticMap, dynamicMap - element.p)
  }

  /**
   * Returns the top element at a given position. When the position is empty, nothing (the [[Empty]] element) is
   * returned. When the location holds a dynamic element it is returned. Otherwise, the static element is returned. When
   * both types are available, the dynamic element is returned (as it is "on top" of the static element).
   *
   * @param p the position
   * @return the top element at the position, or [[Empty]]
   */
  def elementAt(p: Point): Element = dynamicMap.getOrElse(p, staticMap.getOrElse(p, Empty))

  def outOfBounds(p: Point): Boolean = {
    staticMap.contains(p) && staticMap(p).isInstanceOf[Wall]
  }

  /**
   * Calculates a random location on the board that is free.
   *
   * @return a free location on the board
   */
  @tailrec
  final def freeLocation(): Point = {
    val x: Int = Random.nextInt(cols)
    val y: Int = Random.nextInt(rows)
    val newPoint = Point(x, y)
    if (!staticMap.contains(newPoint)) newPoint else freeLocation()
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
