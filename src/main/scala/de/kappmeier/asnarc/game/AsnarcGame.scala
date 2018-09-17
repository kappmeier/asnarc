package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{Body, StaticElement}
import de.kappmeier.asnarc.entity.{Entity, TimedEntity}

import scala.collection.mutable

/**
  * Collection of state for the snake game that is to be modified by [[Entity]].
  */
trait AsnarcGame {
  // Step time in milliseconds
  val stepTime: Int

  val player: mutable.Queue[Body]

  var state: AsnarcState

  var board: AsnarcBoard

  // Game data
  def time(): Int

  def addTimer(te: TimedEntity): Unit

  // Player element
  def direction(): Direction
}
