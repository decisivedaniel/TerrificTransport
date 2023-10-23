package transport.logic

case class TrainFrame(cursor : Point, paths : List[Point]) {
  def moveCursor(d: Direction, gridDim: Dimensions): TrainFrame = {
    copy(gridDim.withinBounds(cursor + d.toPoint))
  }

  def getCellType(p: Point): CellType = {
    if (cursor == p) Cursor()
    else if (paths.contains(p)) Track()
    else Empty()
  }

  def placeTrackOnCursor() : TrainFrame = {
    copy(paths = paths.appended(cursor))
  }
}
