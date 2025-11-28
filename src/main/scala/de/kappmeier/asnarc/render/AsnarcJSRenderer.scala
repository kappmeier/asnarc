package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.elements._

import scala.collection.immutable.HashMap

/**
 * General renderer values.
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

  val InformationStyle: RendererTextStyle = RendererTextStyle("sans-serif", 12, "black")
  val InfoFont: RendererTextStyle = RendererTextStyle("sans-serif", 20, "black")

  val Radius: Int = 5
  val Border: Int = 1
  val DrawRadius: Int = Radius - Border
  val DrawSize: Int = 2 * DrawRadius
  val Size: Int = 2 * Radius

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
