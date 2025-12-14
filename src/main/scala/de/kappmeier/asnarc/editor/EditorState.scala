package de.kappmeier.asnarc.editor

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.elements.{Element, Empty, Teleport, Wall}

/**
  * State of the edited level.
  *
  * @param width    width of the board
  * @param height   height of the board
  * @param cells    map of all [[Element]]s by their position
  * @param unpaired optionally an unpaired teleport position
  */
case class EditorState(width: Int, height: Int,
                       cells: Map[Point, Element] = Map.empty,
                       unpaired: Option[Point] = None) {

  /**
    * Shuffles the [[Element]] type at a specific position.
    *
    * Empty → Wall → Teleport → Empty
    *
    * @param p position to rotate
    * @return the updated editor state with the rotated cell
    */
  def rotateCellAt(p: Point): EditorState = {
    val currentType = cells.getOrElse(p, Empty)
    currentType match {
      case Empty => copy(cells = cells + (p -> Wall(p, Set.empty)))
      case Wall(_, _) => addTeleport(p)
      case Teleport(_, _, _) => removeTeleport(p)
    }
  }

  /**
    * Adds a teleport at the given position, pairing it with an existing unpaired teleport if available.
    *
    * @param p the position to add the teleport
    * @return the updated editor state with the added teleport
    */
  private def addTeleport(p: Point): EditorState = {
    unpaired match {
      case None =>
        // Adding the first teleport element, mark as unpaired
        copy(
          cells = cells + (p -> Teleport(p, Set.empty, None)),
          unpaired = Some(p)
          )
      case Some(other) =>
        // Adding the second teleport element, pair with the unpaired one
        copy(
          cells = cells
            + (p -> Teleport(p, Set.empty, Some(other)))
            + (other -> Teleport(other, Set.empty, Some(p))),
          unpaired = None
          )
    }
  }

  /**
    * Breaks up a teleport pair with one teleport at the given position.
    *
    * @param p the position of one of the teleports to be broken up
    * @return the updated editor state with one removed teleport and one unpaired teleport (if applicable)
    */
  private def removeTeleport(p: Point): EditorState = {
    cells.get(p) match {
      case Some(Teleport(_, _, Some(partner))) =>
        // Break up pair, leave partner as unpaired
        copy(
          cells = cells - p + (partner -> Teleport(partner, Set.empty, None)),
          unpaired = Some(partner)
          )
      case Some(Teleport(_, _, None)) =>
        // Remove unpaired teleport
        copy(
          cells = cells - p,
          // TODO: this forces the user to remove unpaired teleports one by one
          unpaired = if (unpaired.contains(p)) {
            None
          } else {
            unpaired
          })
      case _ =>
        copy(cells = cells - p)
    }
  }
}
