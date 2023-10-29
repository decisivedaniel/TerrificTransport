package transport.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import transport.logic.GameLogic._
import transport.logic.resources.KeyPoint

import scala.collection.immutable.Queue

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {
  private var currentFrame : TrainFrame = TrainFrame(Point(0,0), List[Point](), List[Queue[Point]](), GameLogic.CreateCities(gridDims, random.randomInt), 1000, 0)
  private var isPlacingTrack : Boolean = false
  private var isBuilding: Boolean = false
  private var isPathing: Boolean = false
  private var currentRoute: Queue[Point] = Queue[Point]()

  def gameOver: Boolean = false

  def isBuildMode: Boolean = isBuilding

  def isPathMode: Boolean = isPathing

  def getGuiInfo: String = currentFrame.topGuiInfo()

  // TODO implement me
  def step(): Unit = ()

  // TODO implement me
  def moveCursor(d: Direction): Unit = {
    if (isPathMode){
      val currentCursor = currentFrame.getCursor
      currentFrame = currentFrame.tryMoveCursor(d, gridDims)
      if (currentCursor != currentFrame.getCursor) {
        currentRoute = currentRoute.prepended(currentFrame.getCursor)
      }
    } else {
      currentFrame = currentFrame.moveCursor(d, gridDims)
    }
  }

  def placeTrack() : Unit = if (isBuildMode) currentFrame = currentFrame.toggleTrackOnCursor()

  def buildModeToggle(): Unit = isBuilding = !isPathing && !isBuilding
  def pathModeToggle(): Unit = {
    // Handle Cancelling Path
    if(isPathing){
      currentRoute = Queue[Point]()
    }
    isPathing = !isBuilding && !isPathing
  }

  def completePath(): Unit = {
    if (currentRoute.nonEmpty && currentRoute.front == currentFrame.getCursor) {
      currentFrame = currentFrame.copy(routes = currentFrame.routes.prepended(currentRoute))
      currentRoute = Queue[Point]()
      pathModeToggle()
    }
  }

  // TODO implement me
  def getCellType(p : Point): CellType = {
    if(currentRoute.contains(p)) {
      Route()
    } else {
      currentFrame.getCellType(p)
    }
  }

  // TODO implement me
  def setReverse(r: Boolean): Unit = ()

}

/** GameLogic companion object */
object GameLogic {

  val FramesPerSecond: Int = 5 // change this to increase/decrease speed of game

  val DrawSizeFactor = 1.5 // increase this to make the game bigger (for high-res screens)
  // or decrease to make game smaller

  // These are the dimensions used when playing the game.
  // When testing the game, other dimensions are passed to
  // the constructor of GameLogic.
  //
  // DO NOT USE the variable DefaultGridDims in your code!
  //
  // Doing so will cause tests which have different dimensions to FAIL!
  //
  // In your code only use gridDims.width and gridDims.height
  // do NOT use DefaultGridDims.width and DefaultGridDims.height
  val DefaultGridDims
    : Dimensions =
    Dimensions(width = 25, height = 25)  // you can adjust these values to play on a different sized board

  def CreateCities(gridDims: Dimensions, randomInt: Int => Int) : List[KeyPoint] = {
    var allPoints = gridDims.allPointsInside.filter(p => gridDims.awayFromBounds(p))
    var cities = List[KeyPoint]()
    val numOfTotalCities = randomInt(5) + 5
    while (numOfTotalCities - cities.length > 0) {
      val spot = allPoints(randomInt(allPoints.length))
      cities = cities.prepended(KeyPoint.buildCity(spot))
      allPoints = allPoints.filter(p => p != spot)
    }
    cities
  }


}


