package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.LevelGenerator
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{Element, Empty, StaticElement, Wall}

import scala.collection.mutable
import scala.util.Random

class AsnarcBoard(val map: scala.collection.immutable.Map[Point, StaticElement]) {


  def this(level: String) {
    this(AsnarcBoard.getFromLevel(level))
  }

  val rows = 40
  val cols = 60

  // Board data
  def addElement(p: Point, element: StaticElement): AsnarcBoard = {
    new AsnarcBoard(map + (p -> element))
  }

  def removeElement(p: Point): AsnarcBoard = {
    new AsnarcBoard(map - p)
  }

  def elementAt(p: Point): StaticElement = map.getOrElse(p, Empty)

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
  private def getFromLevel(level: String): scala.collection.immutable.Map[Point, StaticElement] = {
    val mutableMap: scala.collection.mutable.HashMap[Point, StaticElement] = new mutable.HashMap[Point, StaticElement]()

    // Init
    val levelGenerator = new LevelGenerator()

    val wall: mutable.Queue[Element] = levelGenerator.generateBoard64(level)
    wall.foreach(element => mutableMap += (element.p -> element.asInstanceOf[Wall]))

    mutableMap.toMap
  }

}
