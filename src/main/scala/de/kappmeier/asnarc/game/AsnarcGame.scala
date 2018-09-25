package de.kappmeier.asnarc.game

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.entity.Entity

/**
  * Collection of state for the snake game that is to be modified by [[Entity]].
  */
trait AsnarcGame {
  // Step time in milliseconds
  val stepTime: Int
}
