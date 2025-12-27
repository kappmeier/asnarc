package de.kappmeier.asnarc

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

import org.scalajs.dom
import org.scalajs.dom.{document, html}

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.editor.{EditorState, TeleportPairManager}
import de.kappmeier.asnarc.elements.Wall
import de.kappmeier.asnarc.render.AsnarcJSEditorRenderer
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe

/**
  * The ScalaJS export for Asnarc editor. Renders the board, waits for user input and updates the board.
  *
  * The visualization consists of a rectangular field.
  */
@JSExportTopLevel("AsnarcJSEditor")
object AsnarcJSEditor {

  private var state: EditorState = EditorState(20, 15)
  private var renderer: AsnarcJSEditorRenderer = _
  private var boardCanvas: html.Canvas = _
  private var detailsContainer: html.Div = _
  private var statusLabel: html.Span = _

  private val MinimumSize = 5
  private val MaximumSize = 100

  /**
    * Alternative entry point that uses existing canvas elements from the HTML.
    *
    * @param board   the main canvas for the game board
    * @param details the container for element details
    * @param level   base64 encoded level string
    */
  @JSExport
  def main(board: html.Canvas, details: html.Div, level: String): Unit = {
    println(s"Loading level...")
    state = EditorState.fromLevel(level)
    println(s"Loaded a board of size ${state.width}x${state.height}")

    boardCanvas = board
    detailsContainer = details

    setupUI(detailsContainer)

    val localization = new AsnarcLocalizationDe
    renderer = new AsnarcJSEditorRenderer(boardCanvas, localization)

    boardCanvas.onclick = (e: dom.MouseEvent) => handleCanvasClick(e)

    render()
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
      render()
    }

    statusLabel = document.createElement("span").asInstanceOf[html.Span]
    statusLabel.className = "editor-status"

    controls.appendChild(widthInput.parentElement)
    controls.appendChild(heightInput.parentElement)
    controls.appendChild(createButton)
    controls.appendChild(statusLabel)

    container.appendChild(controls)
    boardCanvas.onclick = (e: dom.MouseEvent) => handleCanvasClick(e)
  }

  private def handleCanvasClick(e: dom.MouseEvent): Unit = {
    val cellSize = Math.min((boardCanvas.width / state.width), (boardCanvas.height / state.height))

    val rect = boardCanvas.getBoundingClientRect()
    val x = ((e.clientX - rect.left) / cellSize).toInt
    val y = ((e.clientY - rect.top) / cellSize).toInt
    val point = Point(x, y)

    if (x >= 0 && x < state.width && y >= 0 && y < state.height) {
      state = state.rotateCellAt(point)
      render()
      renderer.highlight(x, y)
    }
  }

  private def render(): Unit = {
    val board = EditorState.toAsnarcBoard(state)
    renderer.renderBoard(board, s"${state.width}x${state.height}")
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

  private def updateStatus(): Unit = {
    val pairs = TeleportPairManager.findPairs(state)
    val unpaired = TeleportPairManager.findUnpaired(state)
    val wallCount = state.cells.count(_._2.isInstanceOf[Wall])

    val statusText = new StringBuilder()
    statusText.append(s"Size: ${state.width}x${state.height} | ")
    statusText.append(s"Walls: $wallCount | ")
    statusText.append(s"Teleport pairs: ${pairs.size}")
    statusText.append(unpaired.map(u => s" | ⚠ Unpaired teleport at $u").getOrElse(""))

    statusLabel.textContent = statusText.toString()
    statusLabel.className = s"editor-status ${
      if (unpaired.isDefined) {
        "status-warning"
      } else {
        "status-ok"
      }
    }"
  }
}
