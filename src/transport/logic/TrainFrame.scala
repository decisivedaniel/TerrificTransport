package transport.logic

import transport.logic.resources.KeyPoint

import scala.collection.immutable.Queue

case class TrainFrame(cursor: Point, paths: List[Point], routes: List[Queue[Point]], keyPoints: List[KeyPoint], money: Int, score: Int) {
  private val costOfTrack: Int = 10
  private val sellPenalty: Int = 1

  def topGuiInfo() : String = s"Money: $money Score: $score"

  def moveCursor(d: Direction, gridDim: Dimensions): TrainFrame = {
    copy(gridDim.withinBounds(cursor + d.toPoint))
  }

  def getCursor(): Point = cursor

  def getCellType(p: Point): CellType = {
    if (cursor == p) Cursor()
    else if (keyPoints.exists(kp => kp.isLocatedHere(p))) {
      val kp = keyPoints.find(kp => kp.isLocatedHere(p)).head
      DisplayCity(kp.toString)
    } else if (routes.exists(route => route.contains(p))) Route()
    else if (paths.contains(p)) Track()
    else Empty()
  }

  def toggleTrackOnCursor() : TrainFrame = {
    if (paths.contains(cursor)) {
      if (money > costOfTrack){
        copy(paths = paths.filter(p => p != cursor), money = money + costOfTrack - sellPenalty)
      } else {
        this
      }
    }
    else copy(paths = paths.appended(cursor), money = money - costOfTrack)
  }
}
