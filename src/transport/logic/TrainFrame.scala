package transport.logic

case class TrainFrame(cursor : Point) {
  def moveCursor(d: Direction, gridDim: Dimensions): TrainFrame = {
    copy(gridDim.withinBounds(cursor + d.toPoint))
  }

  def getCellType(p: Point): CellType = {
    if (cursor == p) Cursor()
    else Empty()
  }
}
