package de.kappmeier.asnarc

import scala.scalajs.js.annotation.{JSExport, JSExportTopLevel}

import org.scalajs.dom
import org.scalajs.dom.{document, html}

import de.kappmeier.asnarc.board.Point
import de.kappmeier.asnarc.editor.{EditorState, TeleportPairManager}
import de.kappmeier.asnarc.elements.Wall
import de.kappmeier.asnarc.levels.PredefinedLevels
import de.kappmeier.asnarc.render.localization.AsnarcLocalizationDe
import de.kappmeier.asnarc.render.{AsnarcJSEditorRenderer, AsnarcJSRenderer}

/**
  * The ScalaJS export for Asnarc editor. Renders the board, waits for user input and updates the board.
  *
  * The visualization consists of a rectangular field.
  *
  * The level parameter can be either:
  * - A named level (e.g., "Empty", see [[PredefinedLevels]] for available levels)
  * - A raw Base64 encoded level string
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
    * @param board     the main canvas for the game board
    * @param details   the container for element details
    * @param level     level name (see [[PredefinedLevels]]) or base64 encoded level string
    * @param blockSize the size of each block in pixels
    */
  @JSExport
  def main(board: html.Canvas, details: html.Div, level: String, blockSize: Int): Unit = {
    // Resolve level input - either a named level or raw level data
    println(s"Loading level...")
    val resolvedLevel: String = PredefinedLevels.resolve(level) match {
      case Some(decodedLevel) => {
        dom.console.log(s"Using named level: ${level}")
        decodedLevel
      }
      case None => {
        dom.console.log(s"Using custom level data")
        level
      }
    }

    state = EditorState.fromLevel(resolvedLevel)
    println(s"Loaded a board of size ${state.width}x${state.height}")

    boardCanvas = board
    detailsContainer = details

    setupUI(detailsContainer)

    // Create renderer configuration with specified block size
    val rendererConfig = new AsnarcJSRenderer(blockSize)
    val localization = new AsnarcLocalizationDe
    renderer = new AsnarcJSEditorRenderer(boardCanvas, localization, rendererConfig)

    boardCanvas.onclick = (e: dom.MouseEvent) => handleCanvasClick(e)

    resizeCanvasToState()
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
      resizeCanvasToState()
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
    val cellSize = renderer.config.Size

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

  /**
    * Resizes the canvas to the size of the entire board. Stretches the parent container as well by setting CSS styles
    * accordingly.
    */
  private def resizeCanvasToState(): Unit = {
    val config = renderer.config
    val w = config.canvasWidth(state.width)
    val h = config.canvasHeight(state.height)
    boardCanvas.width = w
    boardCanvas.height = h
    boardCanvas.style.width = s"${w}px"
    boardCanvas.style.height = s"${h}px"
    val container = boardCanvas.parentElement
    if (container != null) {
      container.style.width = s"${w}px"
      container.style.height = s"${h}px"
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
