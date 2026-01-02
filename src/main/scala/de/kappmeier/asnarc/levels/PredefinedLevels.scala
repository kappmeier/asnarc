package de.kappmeier.asnarc.levels

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

import de.kappmeier.asnarc.LevelGenerator

/**
  * A collection of predefined levels that can be loaded by name. Each level is stored as a Base64 encoded string.
  */
@JSExportTopLevel("PredefinedLevels")
object PredefinedLevels {

  /**
    * A named level with a display name and the level data.
    *
    * @param name        the display name for the level
    * @param description a short description of the level
    * @param data        the Base64 encoded level data
    */
  case class NamedLevel(name: String, description: String, data: String)

  /**
    * An empty level. (`0,0,`).
    */
  val Empty: NamedLevel = NamedLevel(
    "Empty",
    "Empty level with no walls",
    LevelGenerator.EmptyLevel
    )

  /**
    * A simple border level.
    */
  val Simple: NamedLevel = NamedLevel(
    "Border",
    "A level with walls around the border",
    // Simple border walls encoded
    "MCwwLOKUjDsxLDAs4pSAOzIsMCzilIA7MywwLOKUgDs0LDAs4pSQOzAsMSzilII7NCwxLOKUgjs" +
      "wLDIs4pSUOzEsMizilIA7MiwyLOKUgDszLDIs4pSAOzQsMizilJg="
    )

  /**
    * A level with teleporters.
    */
  val Small: NamedLevel = NamedLevel(
    "Small",
    "A small level with open walls teleport fields",
    "MCwwLOKUjDsxLDAs4pSAOzIsMCzilIA7MywwLOKUgDs0LDAs4pSAOzUsMCzilIA7NiwwLOKUgDs3" +
      "LDAs4pSAOzgsMCzilIA7OSwwLOKUgDsxMCwwLOKUgDsxMSwwLOKUgDsxMiwwLOKUgDsxNywwLOKU" +
      "gDsxOCwwLOKUgDsxOSwwLOKUgDsyMCwwLOKUgDsyMSwwLOKUgDsyMiwwLOKUgDsyMywwLOKUgDsy" +
      "NCwwLOKUgDsyNSwwLOKUgDsyNiwwLOKUgDsyNywwLOKUgDsyOCwwLOKUgDsyOSwwLOKUgDszMCww" +
      "LOKUkDswLDIwLOKUlDsxLDIwLOKUgDsyLDIwLOKUgDszLDIwLOKUgDs0LDIwLOKUgDs1LDIwLOKU" +
      "gDs2LDIwLOKUgDs3LDIwLOKUgDs4LDIwLOKUgDs5LDIwLOKUgDsxMCwyMCzilIA7MTEsMjAs4pSA" +
      "OzEyLDIwLOKUgDsxNywyMCzilIA7MTgsMjAs4pSAOzE5LDIwLOKUgDsyMCwyMCzilIA7MjEsMjAs" +
      "4pSAOzIyLDIwLOKUgDsyMywyMCzilIA7MjQsMjAs4pSAOzI1LDIwLOKUgDsyNiwyMCzilIA7Mjcs" +
      "MjAs4pSAOzI4LDIwLOKUgDsyOSwyMCzilIA7MzAsMjAs4pSYOzAsMSzilII7MCwyLOKUgjswLDMs" +
      "4pSCOzAsNCzilII7MCw1LOKUgjswLDYs4pSCOzAsNyzilII7MCw4LOKUgjswLDEyLOKUgjswLDEz" +
      "LOKUgjswLDE0LOKUgjswLDE1LOKUgjswLDE2LOKUgjswLDE3LOKUgjswLDE4LOKUgjswLDE5LOKU" +
      "gjszMCwxLOKUgjszMCwyLOKUgjszMCwzLOKUgjszMCw0LOKUgjszMCw1LOKUgjszMCw2LOKUgjsz" +
      "MCw3LOKUgjszMCw4LOKUgjszMCwxMizilII7MzAsMTMs4pSCOzMwLDE0LOKUgjszMCwxNSzilII7" +
      "MzAsMTYs4pSCOzMwLDE3LOKUgjszMCwxOCzilII7MzAsMTks4pSCOzEwLDEwLFQ7MjAsMTAsVAo="
    )

  /**
    * A maze-like level.
    */
  val Big: NamedLevel = NamedLevel(
    "Big",
    "A bigger level with open walls and teleport fields",
    "MCwwLOKUjDsxLDAs4pSAOzIsMCzilIA7MywwLOKUgDs0LDAs4pSAOzUsMCzilIA7NiwwLOKUgDs3" +
      "LDAs4pSAOzgsMCzilIA7OSwwLOKUgDsxMCwwLOKUgDsxMSwwLOKUgDsxMiwwLOKUgDsxMywwLOKU" +
      "gDsxNCwwLOKUgDsxNSwwLOKUgDsxNiwwLOKUgDsxNywwLOKUgDsxOCwwLOKUgDsxOSwwLOKUgDsy" +
      "MCwwLOKUgDsyMSwwLOKUgDsyMiwwLOKUgDsyMywwLOKUgDsyNCwwLOKUgDsyNSwwLOKUgDsyNiww" +
      "LOKUgDszMywwLOKUgDszNCwwLOKUgDszNSwwLOKUgDszNiwwLOKUgDszNywwLOKUgDszOCwwLOKU" +
      "gDszOSwwLOKUgDs0MCwwLOKUgDs0MSwwLOKUgDs0MiwwLOKUgDs0MywwLOKUgDs0NCwwLOKUgDs0" +
      "NSwwLOKUgDs0NiwwLOKUgDs0NywwLOKUgDs0OCwwLOKUgDs0OSwwLOKUgDs1MCwwLOKUgDs1MSww" +
      "LOKUgDs1MiwwLOKUgDs1MywwLOKUgDs1NCwwLOKUgDs1NSwwLOKUgDs1NiwwLOKUgDs1NywwLOKU" +
      "gDs1OCwwLOKUgDs1OSwwLOKUkDswLDM5LOKUlDsxLDM5LOKUgDsyLDM5LOKUgDszLDM5LOKUgDs0" +
      "LDM5LOKUgDs1LDM5LOKUgDs2LDM5LOKUgDs3LDM5LOKUgDs4LDM5LOKUgDs5LDM5LOKUgDsxMCwz" +
      "OSzilIA7MTEsMzks4pSAOzEyLDM5LOKUgDsxMywzOSzilIA7MTQsMzks4pSAOzE1LDM5LOKUgDsx" +
      "NiwzOSzilIA7MTcsMzks4pSAOzE4LDM5LOKUgDsxOSwzOSzilIA7MjAsMzks4pSAOzIxLDM5LOKU" +
      "gDsyMiwzOSzilIA7MjMsMzks4pSAOzI0LDM5LOKUgDsyNSwzOSzilIA7MjYsMzks4pSAOzMzLDM5" +
      "LOKUgDszNCwzOSzilIA7MzUsMzks4pSAOzM2LDM5LOKUgDszNywzOSzilIA7MzgsMzks4pSAOzM5" +
      "LDM5LOKUgDs0MCwzOSzilIA7NDEsMzks4pSAOzQyLDM5LOKUgDs0MywzOSzilIA7NDQsMzks4pSA" +
      "OzQ1LDM5LOKUgDs0NiwzOSzilIA7NDcsMzks4pSAOzQ4LDM5LOKUgDs0OSwzOSzilIA7NTAsMzks" +
      "4pSAOzUxLDM5LOKUgDs1MiwzOSzilIA7NTMsMzks4pSAOzU0LDM5LOKUgDs1NSwzOSzilIA7NTYs" +
      "Mzks4pSAOzU3LDM5LOKUgDs1OCwzOSzilIA7NTksMzks4pSYOzAsMSzilII7MCwyLOKUgjswLDMs" +
      "4pSCOzAsNCzilII7MCw1LOKUgjswLDYs4pSCOzAsNyzilII7MCw4LOKUgjswLDks4pSCOzAsMTAs" +
      "4pSCOzAsMTEs4pSCOzAsMTIs4pSCOzAsMTMs4pSCOzAsMTQs4pSCOzAsMTUs4pSCOzAsMTYs4pSC" +
      "OzAsMjMs4pSCOzAsMjQs4pSCOzAsMjUs4pSCOzAsMjYs4pSCOzAsMjcs4pSCOzAsMjgs4pSCOzAs" +
      "Mjks4pSCOzAsMzAs4pSCOzAsMzEs4pSCOzAsMzIs4pSCOzAsMzMs4pSCOzAsMzQs4pSCOzAsMzUs" +
      "4pSCOzAsMzYs4pSCOzAsMzcs4pSCOzAsMzgs4pSCOzU5LDEs4pSCOzU5LDIs4pSCOzU5LDMs4pSC" +
      "OzU5LDQs4pSCOzU5LDUs4pSCOzU5LDYs4pSCOzU5LDcs4pSCOzU5LDgs4pSCOzU5LDks4pSCOzU5" +
      "LDEwLOKUgjs1OSwxMSzilII7NTksMTIs4pSCOzU5LDEzLOKUgjs1OSwxNCzilII7NTksMTUs4pSC" +
      "OzU5LDE2LOKUgjs1OSwyMyzilII7NTksMjQs4pSCOzU5LDI1LOKUgjs1OSwyNizilII7NTksMjcs" +
      "4pSCOzU5LDI4LOKUgjs1OSwyOSzilII7NTksMzAs4pSCOzU5LDMxLOKUgjs1OSwzMizilII7NTks" +
      "MzMs4pSCOzU5LDM0LOKUgjs1OSwzNSzilII7NTksMzYs4pSCOzU5LDM3LOKUgjs1OSwzOCzilII7" +
      "MjAsMjAsVDs0MCwyMCxUCg=="
    )

  /**
    * All available predefined levels.
    */
  val All: Seq[NamedLevel] = Seq(Empty, Simple, Small, Big)


  /**
    * Get a level by name (case-insensitive).
    *
    * @param name the name of the level
    * @return Some(level) if found, None otherwise
    */
  def getByName(name: String): Option[NamedLevel] = {
    All.find(_.name.equalsIgnoreCase(name))
  }

  /**
    * Resolve a level input - either a named level or a raw level string.
    * If the input matches a known level name, return the level data.
    * Otherwise, assume the input is raw level data and return it as-is.
    *
    * @param input the level name or raw level data
    * @return the level data string if it exists
    */
  def resolve(input: String): Option[String] = {
    getByName(input).map(_.data)
  }

  /**
    * Returns a list of all available predefined level names.
    */
  @JSExport
  def availableLevels(): js.Array[String] = {
    js.Array(PredefinedLevels.All.map(_.name): _*)
  }
}
