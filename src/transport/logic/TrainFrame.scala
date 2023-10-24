package transport.logic

import transport.logic.resources.KeyPoint

case class TrainFrame(cursor: Point, paths: List[Point], keyPoints: List[KeyPoint]) {
  def moveCursor(d: Direction, gridDim: Dimensions): TrainFrame = {
    copy(gridDim.withinBounds(cursor + d.toPoint))
  }

  def getCellType(p: Point): CellType = {
    if (cursor == p) Cursor()
    else if (paths.contains(p)) Track()
    else Empty()
  }

  def toggleTrackOnCursor() : TrainFrame = {
    if (paths.contains(cursor)) copy(paths = paths.filter(p => p != cursor))
    else copy(paths = paths.appended(cursor))
  }
}
