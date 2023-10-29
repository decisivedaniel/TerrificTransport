// DO NOT MODIFY FOR BASIC SUBMISSION
// scalastyle:off

package transport.game

import java.awt.event
import processing.core.{PApplet, PConstants}
import processing.event.KeyEvent

import java.awt.event.KeyEvent._
import engine.GameBase
import engine.graphics.{Color, Point, Rectangle}
import transport.logic.{CellType, Cursor, Dimensions, Direction, DisplayCity, East, Empty, GameLogic, North, Route, South, Track, West, Point => GridPoint}
import transport.game.TransportGame._
import engine.graphics.Color._
import engine.random.ScalaRandomGen

class TransportGame extends GameBase {

  var gameLogic = new GameLogic(new ScalaRandomGen(),GameLogic.DefaultGridDims)
  val updateTimer = new UpdateTimer(GameLogic.FramesPerSecond.toFloat)
  val gridDimensions : Dimensions =  gameLogic.gridDims
  val widthInPixels: Int = (GameLogic.DrawSizeFactor * WidthCellInPixels * gridDimensions.width).ceil.toInt
  val heightInPixels: Int = (GameLogic.DrawSizeFactor *  HeightCellInPixels * gridDimensions.height).ceil.toInt
  val screenArea: Rectangle = Rectangle(Point(0, 0), widthInPixels.toFloat, heightInPixels.toFloat)

  //String Constants
  private val normalRunInstructions: String = "B: Build Mode, P: Path Mode"
  private val buildModeInstructions: String = "T: Lay Track, B: Exit Build Mode"
  private val pathModeInstructions: String = "C: Complete, P: Exit Path Mode/Cancel"

  // this function is wrongly named draw by processing (is called on each update next to drawing)
  override def draw(): Unit = {
    updateState()
    drawGrid()
    if (gameLogic.gameOver) drawGameOverScreen()
  }

  def drawTextAbove(text: String, point: Point): Unit = {
    stroke(255, 255, 255, 255)
    setFillColor(Color.White)
    drawText(text, Point(point.x - (text.length * 1.3f), point.y - 5f))
  }

  def drawGameOverScreen(): Unit = {
    setFillColor(Red)
    drawTextCentered("GAME OVER!", 20, screenArea.center)
  }

  def drawGrid(): Unit = {

    val widthPerCell = screenArea.width / gridDimensions.width
    val heightPerCell = screenArea.height / gridDimensions.height

    def getCell(p : GridPoint): Rectangle = {
      val leftUp = Point(screenArea.left + p.x * widthPerCell,
        screenArea.top + p.y * heightPerCell)
      Rectangle(leftUp, widthPerCell, heightPerCell)
    }

    def buildBorder(color: Color): Unit = {
      setFillColor(color.copy(alpha = 100))
      rect(screenArea.leftUp.x, screenArea.leftUp.y, widthPerCell, screenArea.height)
      rect(screenArea.leftUp.x, screenArea.leftUp.y, screenArea.width, heightPerCell)
      rect(screenArea.rightUp.x - widthPerCell, screenArea.rightUp.y, widthPerCell, screenArea.height)
      rect(screenArea.leftDown.x, screenArea.leftDown.y - heightPerCell, screenArea.width, heightPerCell)
    }

    def buildTrack(area: Rectangle): Unit = {
      stroke(255, 255, 255, 255)
      setFillColor(Color(89, 59, 25, 255))
      val thickness: Int = (widthPerCell * 0.1).toInt
      rect(area.centerUp.x - thickness, area.centerUp.y, thickness * 2, area.height)
      rect(area.centerLeft.x, area.centerLeft.y - thickness, width, thickness * 2)
    }

    def drawGUI() : Unit = {
      // Add Score, Money
      drawTextAbove(gameLogic.getGuiInfo, screenArea.centerUp.copy(y = screenArea.centerUp.y + heightPerCell))
      if (gameLogic.isBuildMode) {
        // Show T, B exit
        drawTextAbove(buildModeInstructions, screenArea.centerDown)
        // Create Green border
        buildBorder(Color.Green)
      } else if (gameLogic.isPathMode) {
        drawTextAbove(pathModeInstructions, screenArea.centerDown)
        buildBorder(Color.Purple)
      } else {
        // Show B to enter build mode
        drawTextAbove(normalRunInstructions, screenArea.centerDown)
      }
    }

    def drawCell(area: Rectangle, cell: CellType): Unit = {
      // Background
      setFillColor(Color.DarkGreen)
      drawRectangle(area)
      cell match {
        case Cursor() =>
          stroke(255,255,255,255)
          setFillColor(Color(255,255,0,128))
          drawRectangle(area)
        case DisplayCity(title) =>
          setFillColor(Color.Blue)
          drawRectangle(area)
          setFillColor(Color.White)
          drawTextAbove(title, area.leftUp)
//        case SnakeHead(direction) =>
//          setFillColor(Color.LawnGreen)
//          drawTriangle(getTriangleForDirection(direction, area))
//        case SnakeBody(p) =>
//          val color = Color.LawnGreen.interpolate(p,Color.DarkGreen)
//          setFillColor(color)
//          drawRectangle(area)
//        case Apple()  =>
//          setFillColor(Color.Red)
//          drawEllipse(area)
        case Track() =>
          buildTrack(area)
        case Route() =>
          buildTrack(area)
          setFillColor(Color.Gray)
          drawEllipse(area)
        case Empty() =>


      }
    }

    setFillColor(White)
    drawRectangle(screenArea)

    for (p <- gridDimensions.allPointsInside) {
      drawCell(getCell(p), gameLogic.getCellType(p))
    }

    drawGUI()

  }

  /** Method that calls handlers for different key press events.
   * You may add extra functionality for other keys here.
   * See [[event.KeyEvent]] for all defined keycodes.
   *
   * @param event The key press event to handle
   */
  override def keyPressed(event: KeyEvent): Unit = {

    def moveCursor(dir: Direction): Unit =
      gameLogic.moveCursor(dir)

    event.getKeyCode match {
      case VK_UP    => moveCursor(North())
      case VK_DOWN  => moveCursor(South())
      case VK_LEFT  => moveCursor(West())
      case VK_RIGHT => moveCursor(East())
      case VK_R     => gameLogic.setReverse(true)
      case VK_T     => gameLogic.placeTrack()
      case VK_B     => gameLogic.buildModeToggle()
      case VK_P     => gameLogic.pathModeToggle()
      case VK_C     => gameLogic.completePath()
      case _        => ()
    }

  }

  override def keyReleased(event: KeyEvent): Unit = {
    event.getKeyCode match {
      case VK_R => gameLogic.setReverse(false)
      case _    => ()
    }
  }

  override def settings(): Unit = {
    pixelDensity(displayDensity())
    // If line below gives errors try size(widthInPixels, heightInPixels, PConstants.P2D)
    size(widthInPixels, heightInPixels)
  }

  override def setup(): Unit = {
    // Fonts are loaded lazily, so when we call text()
    // for the first time, there is significant lag.
    // This prevents it from happening during gameplay.
    text("", 0, 0)
    // This should be called last, since the game
    // clock is officially ticking at this point
    updateTimer.init()
  }


  def updateState(): Unit = {
    if (updateTimer.timeForNextFrame()) {
      gameLogic.step()
      updateTimer.advanceFrame()
    }
  }

}


object TransportGame {


  val WidthCellInPixels: Double = 20 * GameLogic.DrawSizeFactor
  val HeightCellInPixels: Double = WidthCellInPixels

  def main(args: Array[String]): Unit = {
    // This is needed for Processing, using the name
    // of the class in a string is not very beautiful...
    PApplet.main("transport.game.TransportGame")
  }

}
