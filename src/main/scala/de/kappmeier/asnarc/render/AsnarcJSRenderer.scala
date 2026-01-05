package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.elements._

import scala.collection.immutable.HashMap

/**
 * Renderer configuration with block size and derived values.
 * All values are immutable and derived from the block size at construction time.
 *
 * @param Size the size of a single block in pixels
 */
class AsnarcJSRenderer(val Size: Int = 10) {
  val Border: Int = math.max(1, Size / 10)
  val DrawSize: Int = Size - 2 * Border

  /**
   * Calculate the required canvas width for a board with the given number of columns.
   *
   * @param cols number of columns
   * @return the required canvas width in pixels
   */
  def canvasWidth(cols: Int): Int = cols * Size

  /**
   * Calculate the required canvas height for a board with the given number of rows.
   * Includes space for the info line.
   *
   * @param rows number of rows
   * @return the required canvas height in pixels
   */
  def canvasHeight(rows: Int): Int = rows * Size + AsnarcJSRenderer.infoLineHeight
}

/**
 * Companion object with shared constants like colors.
 */
object AsnarcJSRenderer {

  val DrawColors: HashMap[String, String] = collection.immutable.HashMap[String, String](
    stripSimpleName(Food.getClass.getSimpleName) -> "darkred",
    stripSimpleName(Wall.getClass.getSimpleName) -> "black",
    stripSimpleName(SnakeElement.getClass.getSimpleName) -> "darkgreen",
    stripSimpleName(SpecialFood.getClass.getSimpleName) -> "orange",
    stripSimpleName(SnakeHead.getClass.getSimpleName) -> "green",
    stripSimpleName(Teleport.getClass.getSimpleName) -> "blue"
  )

  private val infoLineHeight: Int = 20

  val InformationStyle: RendererTextStyle = RendererTextStyle("sans-serif", 12, "black")
  val InfoFont: RendererTextStyle = RendererTextStyle("sans-serif", infoLineHeight, "black")

  /**
   * Strips the trailing $ for simple class names of Scala objects. This allows to use the `simpleClassName` from
   * objects as keys inside mappings.
   *
   * @param simpleName the simple name as returned from `x.getClass.getSimpleName`
   * @return the same class name, a trailing $ will be removed if exists
   */
  def stripSimpleName(simpleName: String): String =
    if (simpleName.endsWith("$")) {
      simpleName.init
    } else {
      simpleName
    }
}
