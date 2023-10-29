package transport.logic

// you can alter this file!

case class Dimensions(width : Int, height : Int) {
  // scanned from left to right, top to bottom
  def allPointsInside : Seq[Point] =
    for(y <- 0 until height; x <- 0 until width)
      yield Point(x,y)

  def withinBounds(p: Point): Point = {
    if (p.x < 0) p.copy(x = 0)
    else if (p.y < 0) p.copy(y = 0)
    else if (p.x >= width) p.copy(x = width - 1)
    else if (p.y >= height) p.copy(y = height - 1)
    else p
  }

  def awayFromBounds(p: Point): Boolean = p.x > 1 && p.x < width - 2 && p.y > 1 && p.y < height - 2

}
