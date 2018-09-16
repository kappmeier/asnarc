package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{Body, StaticElement}
import de.kappmeier.asnarc.entity.{Entity, TimedEntity}

import scala.collection.mutable

/**
  * Collection of state for the snake game that is to be modified by [[Entity]].
  */
trait SnakeGame {
  // Step time in milliseconds
  val stepTime: Int
  val snake: mutable.Queue[Body]
  var state: SnakeGameState

  def time(): Int

  def freeLocation(): Point

  def removeElement(p: Point): Unit

  def addElement(p: Point, element: StaticElement): Unit

  def direction(): Direction

  def addTimer(te: TimedEntity): Unit

  def elementAt(p: Point): StaticElement
}
