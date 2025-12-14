package de.kappmeier.asnarc

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.editor.{EditorState, TeleportPairManager}
import de.kappmeier.asnarc.elements.{Empty, Teleport, Wall}
import org.scalajs.dom
import org.scalajs.dom.{document, html}

/**
  * The ScalaJS export for Asnarc editor. Renders the board, waits for user input and updates the board.
  *
  * The visualization consists of a rectangular field.
  */
@JSExportTopLevel("AsnarcJSEditor")
object AsnarcJSEditor {

  private var state: EditorState = EditorState(20, 15)
  private var gridContainer: html.Div = _
  private var statusLabel: html.Span = _

  private val MinimumSize = 5
  private val MaximumSize = 100

  @JSExport
  def main(container: html.Div): Unit = {
    setupUI(container)
    renderGrid()
  }

  private def setupUI(container: html.Div): Unit = {
    container.innerHTML = ""

    // Set up the controls
    val controls = document.createElement("div").asInstanceOf[html.Div]
    controls.className = "editor-controls"

    val widthInput = createNumberInput("Width:", 20)
    val heightInput = createNumberInput("Height:", 15)

    val createButton = document.createElement("button").asInstanceOf[html.Button]
    createButton.textContent = "Create new level"
    createButton.className = "editor-button"
    createButton.onclick = (_: dom.MouseEvent) => {
      val w = widthInput.value.toInt
      val h = heightInput.value.toInt
      state = EditorState(w, h)
      renderGrid()
      updateStatus()
    }

    val exportButton = document.createElement("button").asInstanceOf[html.Button]
    exportButton.textContent = "Export level"
    exportButton.className = "editor-button"
    exportButton.onclick = (_: dom.MouseEvent) => exportLevel()

    statusLabel = document.createElement("span").asInstanceOf[html.Span]
    statusLabel.className = "editor-status"

    controls.appendChild(widthInput.parentElement)
    controls.appendChild(heightInput.parentElement)
    controls.appendChild(createButton)
    controls.appendChild(exportButton)
    controls.appendChild(statusLabel)

    // Set up the grid container
    gridContainer = document.createElement("div").asInstanceOf[html.Div]
    gridContainer.className = "editor-grid-container"

    container.appendChild(controls)
    container.appendChild(gridContainer)

    updateStatus()
  }

  private def createNumberInput(label: String, default: Int): html.Input = {
    val wrapper = document.createElement("div").asInstanceOf[html.Div]
    wrapper.className = "input-wrapper"

    val labelEl = document.createElement("label").asInstanceOf[html.Label]
    labelEl.textContent = label

    val input = document.createElement("input").asInstanceOf[html.Input]
    input.`type` = "number"
    input.value = default.toString
    input.min = MinimumSize.toString
    input.max = MaximumSize.toString
    input.className = "editor-input"

    wrapper.appendChild(labelEl)
    wrapper.appendChild(input)
    input
  }

  private def renderGrid(): Unit = {
    gridContainer.innerHTML = ""

    val grid = document.createElement("div").asInstanceOf[html.Div]
    grid.className = "editor-grid"
    grid.style.setProperty("grid-template-columns", s"repeat(${state.width}, 20px)")
    grid.style.setProperty("grid-template-rows", s"repeat(${state.height}, 20px)")

    // Seems that gridTemplateColumns and gridTemplateRows are not in the Scala.js DOM
    // grid.style.gridTemplateColumns = s"repeat(${state.width}, 20px)"
    // grid.style.gridTemplateRows = s"repeat(${state.height}, 20px)"

    for {
      y <- 0 until state.height
      x <- 0 until state.width
    } {
      val cell = document.createElement("div").asInstanceOf[html.Div]
      val point = Point(x, y)
      cell.className = getCellClass(point)
      cell.dataset.update("x", x.toString)
      cell.dataset.update("y", y.toString)

      cell.onclick = (_: dom.MouseEvent) => {
        state = state.rotateCellAt(point)
        renderGrid()
        updateStatus()
      }

      // Tool tip for teleports
      state.cells.get(point) match {
        case Some(Teleport(_, _, Some(partner))) =>
          cell.title = s"Connected with mit (${partner.x}, ${partner.y})"
        case Some(Teleport(_, _, None)) =>
          cell.title = "Unpaired teleport"
        case _ =>
      }

      grid.appendChild(cell)
    }

    gridContainer.appendChild(grid)
  }

  private def getCellClass(p: Point): String = {
    val baseClass = "editor-cell"
    val typeClass = state.cells.getOrElse(p, Empty) match {
      case Empty => "cell-empty"
      case Wall(_, _) => "cell-wall"
      case Teleport(_, _, None) => "cell-teleport cell-unpaired"
      case Teleport(_, _, Some(_)) => "cell-teleport cell-paired"
    }
    s"$baseClass $typeClass"
  }

  private def updateStatus(): Unit = {
    val pairs = TeleportPairManager.findPairs(state)
    val unpaired = TeleportPairManager.findUnpaired(state)
    val wallCount = state.cells.count(_._2.isInstanceOf[Wall])

    val statusText = new StringBuilder()
    statusText.append(s"Size: ${state.width}x${state.height} | ")
    statusText.append(s"Walls: $wallCount | ")
    statusText.append(s"Teleport pairs: ${pairs.size}")

    if (unpaired.isDefined) {
      statusText.append(s" | ⚠ Unpaired teleport at ${unpaired.get}")
      statusLabel.className = "editor-status status-warning"
    } else {
      statusLabel.className = "editor-status status-ok"
    }

    statusLabel.textContent = statusText.toString()
  }

  private def exportLevel(): Unit = {
    TeleportPairManager.validate(state) match {
      case Left(error) =>
        dom.window.alert(s"Export not possible: $error")
      case Right(_) =>
        dom.window.alert(s"Export not implemented yet.")
    }
  }

}