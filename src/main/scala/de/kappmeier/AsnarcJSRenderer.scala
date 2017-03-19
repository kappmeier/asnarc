package example
import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html

import Direction._

/**
 * Draws the Asnark game into a canvas.
 */
class AsnarcJSRenderer(canvas: html.Canvas) {
    val drawColors = collection.immutable.HashMap[String, String](Food.getClass.getSimpleName -> "darkred",
        Wall.getClass.getSimpleName -> "black",
        Body.getClass.getSimpleName -> "darkgreen",
        SpecialFood.getClass.getSimpleName -> "orange",
        Player.getClass.getSimpleName -> "green"
    )

        val renderer = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]

        canvas.width = canvas.parentElement.clientWidth
        canvas.height = canvas.parentElement.clientHeight

        renderer.font = "20px sans-serif"
        renderer.fillStyle = "#f8f8f8"
        renderer.fillRect(0, 0, canvas.width, canvas.height)

        dom.console.log("w: " + canvas.width)
        dom.console.log("h: " + canvas.height)

        renderer.textAlign = "left"
        renderer.textBaseline = "top"

        val radius = 5
        val border = 1
        val drawRadius = radius - border
        val drawSize = 2 * drawRadius
        val size = 2 * radius

        var counter: Int = 1

     def render(snakeGame: SnakeGameImpl) {
        renderer.clearRect(0, 0, canvas.width, canvas.height)
            
        if (snakeGame.dead) {
            renderMove(snakeGame)
            renderDead(snakeGame)
        } else {
            snakeGame.updateMove()
            if (snakeGame.dead) {
                renderer.textAlign = "center"
                renderer.textBaseline = "middle"
            }

            renderMove(snakeGame)
            renderInfo(snakeGame)
        }
     }
 
    def renderStart(snakeGame: SnakeGameImpl): Unit = {
    
    }
    

    def renderMove(snakeGame: SnakeGameImpl): Unit = {
         // Draw background
         for ((p, e) <- snakeGame.map) {
             val color = drawColors.getOrElse(e.getClass.getSimpleName, "yellow")
             renderer.fillStyle = color
             fillElement(e)
         }
         renderer.fillStyle = drawColors.get(Player.getClass.getSimpleName).get
         fillElement(snakeGame.player)
    }

    def fillField(location: Point): Unit = {
        val x = location.x * size
        val y = location.y * size
        renderer.fillRect(x + border, y + border, drawSize, drawSize)
    }

    def fillFull(location: Point): Unit = {
        val x = location.x * size
        val y = location.y * size
        renderer.fillRect(x, y, size, size)
    }

    def renderInfo(snakeGame: SnakeGameImpl): Unit = {
        val len: Int = snakeGame.snake.size + 1
        renderer.fillText("Länge: " + len, size, snakeGame.rows * size + size)
        renderer.fillText("Punkte: " + 0, size, snakeGame.rows * size + size + 30)
        renderer.fillText("Position: (" + snakeGame.player.p.x + "," + snakeGame.player.p.y + ")", size, snakeGame.rows * size + size + 90)
    }

    def fillElement(location: Element): Unit = {
        val x = if (location.connects contains Direction.LEFT) location.p.x * size else location.p.x * size + border
        val y = if (location.connects contains Direction.UP) location.p.y * size else location.p.y * size + border
        val w = drawSize + (if (location.connects contains Direction.LEFT) 1 else 0) + (if(location.connects contains Direction.RIGHT) 1 else 0)
        val h = drawSize + (if (location.connects contains Direction.UP) 1 else 0) + (if(location.connects contains Direction.DOWN) 1 else 0)
        renderer.fillRect(x, y, w, h)
    }

    def renderDead(snakeGame: SnakeGameImpl): Unit = {
        renderer.fillStyle = "darkred"
        renderer.fillText("Game Over", snakeGame.cols * size / 2, snakeGame.rows * size / 2)
    }
    
    def renderPause(snakeGame: SnakeGameImpl): Unit = {
    
    }
    
    def renderGameOver(snakeGame: SnakeGameImpl): Unit = {
    
    }
}
