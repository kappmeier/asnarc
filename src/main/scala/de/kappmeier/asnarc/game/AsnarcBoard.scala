package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.LevelGenerator
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{Element, Empty, Wall}

import scala.collection.mutable
import scala.util.Random

class AsnarcBoard(val map: scala.collection.immutable.Map[Point, Element]) {


  def this(level: String) {
    this(AsnarcBoard.getFromLevel(level))
  }

  val rows = 40
  val cols = 60

  // Board data
  def addElement(element: Element): AsnarcBoard = {
    new AsnarcBoard(map + (element.p -> element))
  }

  def removeElement(p: Point): AsnarcBoard = {
    new AsnarcBoard(map - p)
  }

  def elementAt(p: Point): Element = map.getOrElse(p, Empty)

  def outOfBounds(p: Point): Boolean = {
    map.contains(p) && map(p).isInstanceOf[Wall]
  }

  def freeLocation(): Point = {
    val x: Int = Random.nextInt(cols)
    val y: Int = Random.nextInt(rows)
    val newPoint = Point(x, y)
    if (map.get(newPoint).isEmpty) newPoint else freeLocation()
  }

}

object AsnarcBoard {
  private def getFromLevel(level: String): scala.collection.immutable.Map[Point, Element] = {
    val mutableMap: scala.collection.mutable.HashMap[Point, Element] = new mutable.HashMap[Point, Element]()

    // Init
    val levelGenerator = new LevelGenerator()

    val wall: mutable.Queue[Element] = levelGenerator.generateBoard64(level)
    wall.foreach(element => mutableMap += (element.p -> element.asInstanceOf[Wall]))

    mutableMap.toMap
  }

}
