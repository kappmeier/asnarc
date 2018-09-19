package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.entity.{Entity, Player, TimedEntity}

import scala.collection.immutable.HashSet

/**
  * Collection of state for the snake game that is to be modified by [[Entity]].
  */
trait AsnarcGame {
  // Step time in milliseconds
  val stepTime: Int

  val player: Player

  var state: AsnarcWorld

  var board: AsnarcBoard

  var entities: HashSet[Entity]

  // Game data
  def time(): Int

  def addTimer(te: TimedEntity): Unit

  // Player element
  def direction(): Direction
}
