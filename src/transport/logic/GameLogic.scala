package transport.logic

import engine.random.{RandomGenerator, ScalaRandomGen}
import transport.logic.GameLogic._
import transport.logic.resources.KeyPoint

/** To implement Snake, complete the ``TODOs`` below.
 *
 * If you need additional files,
 * please also put them in the ``snake`` package.
 */
class GameLogic(val random: RandomGenerator,
                val gridDims : Dimensions) {
  private var currentFrame : TrainFrame = TrainFrame(Point(0,0), List[Point](), GameLogic.CreateCities(gridDims, random.randomInt))
  private var isPlacingTrack : Boolean = false

  def gameOver: Boolean = false

  // TODO implement me
  def step(): Unit = ()

  // TODO implement me
  def moveCursor(d: Direction): Unit = {
    currentFrame = currentFrame.moveCursor(d, gridDims)
    if (isPlacingTrack) currentFrame = currentFrame.toggleTrackOnCursor()
  }

  def placeTrack() : Unit = isPlacingTrack = !isPlacingTrack

  // TODO implement me
  def getCellType(p : Point): CellType = currentFrame.getCellType(p)

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
    var allPoints = gridDims.allPointsInside
    val cities = List[KeyPoint]()
    val numOfCities = randomInt(5) + 5
    while (numOfCities > 0) {
      val spot = allPoints(randomInt(allPoints.length))
      cities :+ KeyPoint.buildCity(spot)
      allPoints = allPoints.filter(p => p != spot)
    }
    cities
  }


}


