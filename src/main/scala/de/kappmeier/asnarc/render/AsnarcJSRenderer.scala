package de.kappmeier.asnarc.render

import de.kappmeier.asnarc.elements._

import scala.collection.immutable.HashMap

/**
  * General renderer values.
  */
object AsnarcJSRenderer {

  val DrawColors: HashMap[String, String] = collection.immutable.HashMap[String, String](
    Food.getClass.getSimpleName -> "darkred",
    Wall.getClass.getSimpleName -> "black",
    SnakeElement.getClass.getSimpleName -> "darkgreen",
    SpecialFood.getClass.getSimpleName -> "orange",
    SnakeHead.getClass.getSimpleName -> "green"
  )

  val InformationStyle: RendererTextStyle = RendererTextStyle("sans-serif", 12, "black")
  val InfoFont: RendererTextStyle = RendererTextStyle("sans-serif", 20, "black")

  val Radius: Int = 5
  val Border: Int = 1
  val DrawRadius: Int = Radius - Border
  val DrawSize: Int = 2 * DrawRadius
  val Size: Int = 2 * Radius

}
