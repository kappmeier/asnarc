package de.kappmeier.asnarc.editor

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.Teleport

/**
  * Utility to manage teleport pairings.
  */
object TeleportPairManager {

  /**
    * Collects all teleport pairs.
    *
    * @param state the current editor state
    * @return Sequence of teleport pairs (each pair as a tuple of Points)
    */
  def findPairs(state: EditorState): Seq[(Point, Point)] = {
    state.cells.collect {
      case (p, Teleport(_, _, Some(partner))) if p.x < partner.x || (p.x == partner.x && p.y < partner.y) =>
        (p, partner)
    }.toSeq
  }

  /**
    * Finds the unpaired teleport, if it exists.
    */
  def findUnpaired(state: EditorState): Option[Point] = state.unpaired

  /**
    * Validates that no unpaired teleports exist.
    */
  def validate(state: EditorState): Either[String, Unit] = {
    if (state.unpaired.isDefined) {
      Left(s"Unpaired teleport at ${state.unpaired.get}")
    } else {
      Right(())
    }
  }
}
