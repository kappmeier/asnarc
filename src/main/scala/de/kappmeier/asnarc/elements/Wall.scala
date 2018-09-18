package de.kappmeier.asnarc.elements

import de.kappmeier.asnarc.board.Direction.Direction
import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.entity.Entity
import de.kappmeier.asnarc.game.AsnarcGame
import de.kappmeier.asnarc.transitions.{Death, StateTransition}

import scala.collection.immutable.Set

case class Wall(p: Point, connects: Set[Direction]) extends Element
