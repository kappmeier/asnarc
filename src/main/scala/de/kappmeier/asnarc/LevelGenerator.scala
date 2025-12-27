package de.kappmeier.asnarc

import java.util.Base64
import de.kappmeier.asnarc.board.{Direction, Point}
import de.kappmeier.asnarc.board.Direction._
import de.kappmeier.asnarc.elements.{Element, Teleport, Wall}

import scala.collection.immutable.{HashMap, Set}
import scala.collection.mutable

/**
 * A factory that can create [[de.kappmeier.asnarc.game.AsnarcGame]] boards to be used to play. The levels are defined
 * in a text definition, either in clear text or Base 64 encoded. Clear text encoding can introduce encoding problems,
 * such that in cases where UTF-8 is not available Base 64 should be preferred.
 *
 * Each element is represented as a comma separated 3 tuple `x,y,c` where `x` and `y` represent the coordinate of a
 * field and `c` denotes the type of element stored at the location. An example is `0,0,┌`, which places an element into
 * the upper left corner that is connected to the right and bottom elements, i.e. it is actually a corner.
 *
 * The whole description of the board consists of a list of `;` separated list of the aforementioned 3 tuples, i.e.
 * `0,0,┌;59,39,┘;0,1,│`
 *
 * The following types are available: '─', '│', '┌', '┐', '┘', '└', '├', '┬', '┤', '┴', '┼', ' ', 'T'
 *
 * All elements but the last one represent a filled block which is supposed to be connected with 1 or more neighbour
 * blocks. The last block represents an empty field. It is not required to submit empty fields, every field not
 * specified is assumed to be empty.
 */
case class LevelGenerator() {
  /** The decoder. */
  private val decoder: Base64.Decoder = Base64.getDecoder
  /** The encoder. */
  private val encoder: Base64.Encoder = Base64.getEncoder

  /**
   * Generates the fixed level elements belonging to a level given as Base64 encoded `String`. These elements comprise
   * [[Wall]] and [[Teleport]].
   *
   * @param levelInput the input as Base64 encoded string
   * @return the static initial definition of a level
   */
  def generateBoard64Elements(levelInput: String): mutable.Queue[Element] = {
    val decoded = new String(decoder.decode(levelInput))
    generateBoardElements(decoded)
  }

  /**
   * Generates the fixed level elements belonging to a level given as `String`. These elements comprise [[Wall]] and
   * [[Teleport]].
   *
   * @param levelInput the input as string
   * @return the static initial definition of a level
   */
  private def generateBoardElements(levelInput: String): mutable.Queue[Element] = {
    val input: Seq[(Point, Char)] = breakBlocks(levelInput).map(x => breakBlock(x))
    val builder: mutable.Builder[Element, mutable.Queue[Element]] = mutable.Queue.newBuilder
    builder ++= input.map {
      case (location, element) if element == 'T' => Teleport(location, Set(), None)
      case (location, element) => Wall(location, LevelGenerator.BoundMap(element))
    }
    builder.result()
  }

  /**
   * Breaks the level into parts for each row. The level description contains the information for each row separated
   * by `;`.
   *
   * @param input level description for multiple lines
   * @return
   */
  private def breakBlocks(input: String): Seq[String] = input.split(";")

  private def breakBlock(input: String): (Point, Char) = {
    val parts = input.split(",")
    (Point(parts(0).toInt, parts(1).toInt), parts(2).toCharArray.apply(0))
  }
}

object LevelGenerator {
  /**
   * Base64 encoding of an empty level. (`0,0,`).
   */
  val EmptyLevel = "MCwwLCA="

  /**
   * Maps the level description `Char`s to the direction with which the corresponding [[Wall]] will be connected
   * to.
   */
  private val BoundMap: HashMap[Char, Set[Direction]] = collection.immutable.HashMap[Char, Set[Direction]](
    '─' -> Set(Direction.Left, Direction.Right),
    '│' -> Set(Direction.Down, Direction.Up),
    '┌' -> Set(Direction.Down, Direction.Right),
    '┐' -> Set(Direction.Down, Direction.Left),
    '┘' -> Set(Direction.Left, Direction.Up),
    '└' -> Set(Direction.Right, Direction.Up),
    '├' -> Set(Direction.Down, Direction.Right, Up),
    '┬' -> Set(Direction.Down, Direction.Right),
    '┤' -> Set(Direction.Down, Direction.Left, Up),
    '┴' -> Set(Direction.Left, Direction.Right, Up),
    '┼' -> Set(Direction.Down, Direction.Left, Direction.Right, Direction.Up),
    ' ' -> Set()
    )
}
